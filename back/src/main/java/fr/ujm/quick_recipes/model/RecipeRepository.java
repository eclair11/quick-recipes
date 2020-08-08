package fr.ujm.quick_recipes.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to handle the Recipe entity in the database.
 * 
 * @author Elias Romdan
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}