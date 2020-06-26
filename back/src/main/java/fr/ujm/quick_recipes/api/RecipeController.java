package fr.ujm.quick_recipes.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.ujm.quick_recipes.model.Recipe;
import fr.ujm.quick_recipes.model.RecipeRepository;

@RestController
@RequestMapping("/api/v1/recipes")
@CrossOrigin(origins = "http://localhost:4200")
public class RecipeController {

    private static final int PAGE_SIZE = 10;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    RecipeRepository recipeRepo;

    @GetMapping(value = "/categories", produces = { "application/json" })
    public ResponseEntity<List<String>> getListCategories() {
        Query query = entityManager.createQuery("Select Distinct c.name From Category c");
        List<String> categories = castList(String.class, query.getResultList());
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @GetMapping(value = "/regions", produces = { "application/json" })
    public ResponseEntity<List<String>> getListRegions() {
        Query query = entityManager.createQuery("Select Distinct r.region From Recipe r");
        List<String> regions = castList(String.class, query.getResultList());
        return ResponseEntity.status(HttpStatus.OK).body(regions);
    }

    @GetMapping(value = "/list/{type}/{key}/{page}", produces = { "application/json" })
    public ResponseEntity<MultiValueMap<String, Object>> getListRecipes(@PathVariable String type,
            @PathVariable String key, @PathVariable int page) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        String requestRecipes = "Select r.id, r.name, r.picture From Recipe r Where r.name LIKE '%" + key + "%'";
        String requestPages = "Select count(r.id) From Recipe r Where r.name LIKE '%" + key + "%'";
        Query queryRecipes = entityManager.createQuery(requestRecipes);
        Query queryPages = entityManager.createQuery(requestPages);
        queryRecipes.setFirstResult((page - 1) * PAGE_SIZE);
        queryRecipes.setMaxResults(PAGE_SIZE);
        List<Object> recipes = castList(Object.class, queryRecipes.getResultList());
        int pages = (Integer.valueOf(queryPages.getSingleResult().toString()) + PAGE_SIZE - 1) / PAGE_SIZE;
        body.add("recipes", recipes);
        body.add("pages", pages);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping(value = "/{id}", produces = { "application/json" })
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeRepo.findById(id).get();
        Query queryCategory = entityManager.createQuery("Select c.name From Category c Where c.recipe = " + id);
        Query queryPicture = entityManager.createQuery("Select p.name From Picture p Where p.recipe = " + id);
        Query queryIngredient = entityManager.createQuery("Select i.name From Ingredient i Where i.recipe = " + id);
        Query queryPreparation = entityManager.createQuery("Select p.name From Preparation p Where p.recipe = " + id);
        List<String> categories = castList(String.class, queryCategory.getResultList());
        List<String> pictures = castList(String.class, queryPicture.getResultList());
        List<String> ingredients = castList(String.class, queryIngredient.getResultList());
        List<String> preparations = castList(String.class, queryPreparation.getResultList());
        recipe.setCategories(categories);
        recipe.setPictures(pictures);
        recipe.setIngredients(ingredients);
        recipe.setPreparations(preparations);
        return ResponseEntity.status(HttpStatus.OK).body(recipe);
    }

    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<T>(c.size());
        for (Object o : c) {
            r.add(clazz.cast(o));
        }
        return r;
    }

}