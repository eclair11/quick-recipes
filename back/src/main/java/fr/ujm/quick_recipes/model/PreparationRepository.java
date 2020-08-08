package fr.ujm.quick_recipes.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to handle the Preparation entity in the database.
 * 
 * @author Elias Romdan
 */
public interface PreparationRepository extends JpaRepository<Preparation, Long> {

    Long deleteByRecipe(Recipe r);
    
}