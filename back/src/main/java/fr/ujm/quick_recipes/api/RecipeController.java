package fr.ujm.quick_recipes.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    RecipeRepository recipeRepo;

    @GetMapping(value = "/{id}", produces = { "application/json" })
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeRepo.findById(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(recipe);
    }

}