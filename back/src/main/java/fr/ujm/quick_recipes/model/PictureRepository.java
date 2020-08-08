package fr.ujm.quick_recipes.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to handle the Picture entity in the database.
 * 
 * @author Elias Romdan
 */
public interface PictureRepository extends JpaRepository<Picture, Long> {

    Long deleteByRecipe(Recipe r);

}