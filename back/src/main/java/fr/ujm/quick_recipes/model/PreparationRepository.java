package fr.ujm.quick_recipes.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PreparationRepository extends JpaRepository<Preparation, Long> {

    Long deleteByRecipe(Recipe r);
    
}