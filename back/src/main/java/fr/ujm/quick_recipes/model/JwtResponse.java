package fr.ujm.quick_recipes.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = 1616798285881611833L;

    private final String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}