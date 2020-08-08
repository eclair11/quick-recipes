package fr.ujm.quick_recipes.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to handle the Ingredient entity in the database.
 * 
 * @author Elias Romdan
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Long deleteByRecipe(Recipe r);
    
}