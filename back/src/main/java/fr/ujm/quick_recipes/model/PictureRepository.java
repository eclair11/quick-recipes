package fr.ujm.quick_recipes.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    Long deleteByRecipe(Recipe r);
    
}