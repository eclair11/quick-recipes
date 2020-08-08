package fr.ujm.quick_recipes.model;

import java.io.Serializable;

/**
 * Class to handle the authentication request.
 * 
 * @author Elias Romdan
 */
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 6692813234423065516L;

    private String nickname;
    private String password;

    public JwtRequest(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
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

}