package fr.ujm.quick_recipes.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class to handle the User object.
 * 
 * @author Elias Romdan
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nickname;
    private String password;

    @ElementCollection(targetClass = Long.class)
    private List<Long> favorites;

    public User() {
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
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return List<Long>
     */
    public List<Long> getFavorites() {
        return favorites;
    }

    /**
     * @param favorites
     */
    public void setFavorites(List<Long> favorites) {
        this.favorites = favorites;
    }

}