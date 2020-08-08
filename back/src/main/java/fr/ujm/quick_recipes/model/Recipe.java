package fr.ujm.quick_recipes.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Class to handle the Recipe object.
 * 
 * @author Elias Romdan
 */
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String region;
    private String discovery;
    private String author;
    private String calorie;
    private String picture;

    @Column(columnDefinition = "TEXT")
    private String history;

    @Transient
    private List<String> categories;

    @Transient
    private List<String> pictures;

    @Transient
    private List<String> ingredients;

    @Transient
    private List<String> preparations;

    public Recipe() {
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
     * @return String
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return String
     */
    public String getDiscovery() {
        return discovery;
    }

    /**
     * @param discovery
     */
    public void setDiscovery(String discovery) {
        this.discovery = discovery;
    }

    /**
     * @return String
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return String
     */
    public String getCalorie() {
        return calorie;
    }

    /**
     * @param calorie
     */
    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    /**
     * @return String
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * @return String
     */
    public String getHistory() {
        return history;
    }

    /**
     * @param history
     */
    public void setHistory(String history) {
        this.history = history;
    }

    /**
     * @return List<String>
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * @param categories
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    /**
     * @return List<String>
     */
    public List<String> getPictures() {
        return pictures;
    }

    /**
     * @param pictures
     */
    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    /**
     * @return List<String>
     */
    public List<String> getIngredients() {
        return ingredients;
    }

    /**
     * @param ingredients
     */
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * @return List<String>
     */
    public List<String> getPreparations() {
        return preparations;
    }

    /**
     * @param preparations
     */
    public void setPreparations(List<String> preparations) {
        this.preparations = preparations;
    }

}