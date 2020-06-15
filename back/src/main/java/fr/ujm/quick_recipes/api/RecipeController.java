package fr.ujm.quick_recipes.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

    private static final int PAGE_SIZE = 1;

    @Autowired
    RecipeRepository recipeRepo;

    @PersistenceContext
    EntityManager entityManager;

    @GetMapping(value = "/list/{page}", produces = { "application/json" })
    public ResponseEntity<MultiValueMap<String, Object>> getListRecipes(@PathVariable int page) {
        MultiValueMap<String, Object> recipes = new LinkedMultiValueMap<>();
        TypedQuery<Recipe> queryList = entityManager.createQuery("From Recipe", Recipe.class);
        TypedQuery<Long> queryTotal = entityManager.createQuery("Select count(r.id) From Recipe r", Long.class);
        queryList.setFirstResult((page - 1) * PAGE_SIZE);
        queryList.setMaxResults(PAGE_SIZE);
        recipes.add("list", queryList.getResultList());
        recipes.add("total", (queryTotal.getSingleResult() + PAGE_SIZE - 1) / PAGE_SIZE);
        return ResponseEntity.status(HttpStatus.OK).body(recipes);
    }

    @GetMapping(value = "/{id}", produces = { "application/json" })
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeRepo.findById(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(recipe);
    }

}