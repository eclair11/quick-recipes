package fr.ujm.quick_recipes.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Long deleteByRecipe(Recipe r);
    
}