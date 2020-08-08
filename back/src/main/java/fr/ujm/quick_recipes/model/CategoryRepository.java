package fr.ujm.quick_recipes.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to handle the Category entity in the database.
 * 
 * @author Elias Romdan
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Long deleteByRecipe(Recipe r);

}