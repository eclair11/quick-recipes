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

/**
 * REST controller to handle API in relation with the Recipe object.
 * 
 * @author Elias Romdan
 */
@RestController
@RequestMapping("/api/v1/recipes")
@CrossOrigin(origins = "http://localhost:4200")
public class RecipeController {

    /* Set the maximum number of recipes displayed in one page */
    private static final int PAGE_SIZE = 20;

    /* Set the maximum number of categories and regions displayed in the homepage */
    private static final int MAX_SIZE = 25;

    /* Object to handle SQL request to find recipes */
    private String requestRecipes = "";

    /* Object to handle SQL request to find the number of recipes */
    private String requestPages = "";

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    RecipeRepository recipeRepo;

    /**
     * Send the list of all categories found in the database.
     * 
     * @return ResponseEntity<List<String>>
     */
    @GetMapping(value = "/categories", produces = { "application/json" })
    public ResponseEntity<List<String>> getListCategories() {
        Query query = entityManager.createQuery("Select Distinct c.name From Category c Order By Rand()");
        query.setMaxResults(MAX_SIZE);
        List<String> categories = castList(String.class, query.getResultList());
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    /**
     * Send the list of all regions found in the database.
     * 
     * @return ResponseEntity<List<String>>
     */
    @GetMapping(value = "/regions", produces = { "application/json" })
    public ResponseEntity<List<String>> getListRegions() {
        Query query = entityManager.createQuery("Select Distinct r.region From Recipe r Order By Rand()");
        query.setMaxResults(MAX_SIZE);
        List<String> regions = castList(String.class, query.getResultList());
        return ResponseEntity.status(HttpStatus.OK).body(regions);
    }

    /**
     * Send the list of recipes (id, name and cover picture) based on the search
     * made by the user.
     * 
     * @param type the type of the search (by name, by ingredients, by region or by category)
     * @param key the content of the search
     * @param page number of page
     * @return ResponseEntity<MultiValueMap<String, Object>>
     */
    @GetMapping(value = "/list/{type}/{key}/{page}", produces = { "application/json" })
    public ResponseEntity<MultiValueMap<String, Object>> getListRecipes(@PathVariable String type,
            @PathVariable String key, @PathVariable int page) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        String[] keys = key.split("[']");
        key = String.join("''", keys);
        switch (type) {
            case "normal":
                requestNormal(key);
                break;
            case "special":
                requestSpecial(key);
                break;
            case "category":
                requestCategory(key);
                break;
            case "region":
                requestRegion(key);
                break;
            default:
                break;
        }
        Query queryRecipes = entityManager.createQuery(this.requestRecipes);
        Query queryPages = entityManager.createQuery(this.requestPages);
        queryRecipes.setFirstResult((page - 1) * PAGE_SIZE);
        queryRecipes.setMaxResults(PAGE_SIZE);
        List<Object> recipes = castList(Object.class, queryRecipes.getResultList());
        int pages = (Integer.valueOf(queryPages.getSingleResult().toString()) + PAGE_SIZE - 1) / PAGE_SIZE;
        body.add("recipes", recipes);
        body.add("pages", pages);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    /**
     * Send a recipe object (with all informations) based on its identifier.
     * 
     * @param id identifier of the recipe selected by the user
     * @return ResponseEntity<Recipe>
     */
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

    /**
     * SQL request to search recipes based on their name.
     * 
     * @param key name typed by the user
     */
    private void requestNormal(String key) {
        this.requestRecipes = "Select r.id, r.name, r.picture From Recipe r Where r.name LIKE '%" + key + "%'";
        this.requestPages = "Select count(r.id) From Recipe r Where r.name LIKE '%" + key + "%'";
    }

    /**
     * SQL request to search recipes based on their ingredients.
     * 
     * @param key list of ingredients typed by the user
     */
    private void requestSpecial(String key) {
        List<String> keys = new ArrayList<>();
        for (String k : key.split(",")) {
            keys.add("'%" + k.trim() + "%'");
        }
        String ings = String.join(" OR i.name LIKE ", keys);
        this.requestRecipes = "Select Distinct r.id, r.name, r.picture From Recipe r, Ingredient i Where r.id = i.recipe AND "
                + "(i.name LIKE " + ings + ")";
        this.requestPages = "Select Distinct count(r.id) From Recipe r, Ingredient i Where r.id = i.recipe AND "
                + "(i.name LIKE " + ings + ")";
    }

    /**
     * SQL request to search recipes based on their category.
     * 
     * @param key category selected by the user
     */
    private void requestCategory(String key) {
        this.requestRecipes = "Select r.id, r.name, r.picture From Recipe r, Category c Where r.id = c.recipe AND c.name LIKE '%"
                + key + "%'";
        this.requestPages = "Select count(r.id) From Recipe r, Category c Where r.id = c.recipe AND c.name LIKE '%"
                + key + "%'";
    }

    /**
     * SQL request to search recipes based on their region.
     * 
     * @param key region selected by the user
     */
    private void requestRegion(String key) {
        this.requestRecipes = "Select r.id, r.name, r.picture From Recipe r Where r.region LIKE '%" + key + "%'";
        this.requestPages = "Select count(r.id) From Recipe r Where r.region LIKE '%" + key + "%'";
    }

    /**
     * Function to avoid the unchecked conversion warning.
     * 
     * @param clazz
     * @param c
     * @return List<T>
     */
    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<T>(c.size());
        for (Object o : c) {
            r.add(clazz.cast(o));
        }
        return r;
    }

}