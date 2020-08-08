package fr.ujm.quick_recipes.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Class to handle the Category object.
 * 
 * @author Elias Romdan
 */
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    private Recipe recipe;

    public Category() {
    }

    /**
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Recipe
     */
    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * @param recipe
     */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

}