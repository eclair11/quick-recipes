package fr.ujm.quick_recipes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import fr.ujm.quick_recipes.model.JwtRequest;
import fr.ujm.quick_recipes.model.User;

public class UserControllerTests extends AbstractTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void signUp() throws Exception {
        User user = new User();
        user.setNickname("NickNameTest");
        user.setPassword("PassWordTest");
        String body = (new ObjectMapper()).valueToTree(user).toString();
        String uri = "/api/v1/users/signup";
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.post(uri).content(body).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void signIn() throws Exception {
        JwtRequest request = new JwtRequest("NickNameTest", "PassWordTest");
        String body = (new ObjectMapper()).valueToTree(request).toString();
        String uri = "/api/v1/users/signin";
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.post(uri).content(body).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void addFavorite() throws Exception {
        String id = "1";
        String nickname = "NickNameTest";
        String uri = "/api/v1/users/favorite";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri).param("recipe", id).param("user", nickname)
                .contentType(MediaType.MULTIPART_FORM_DATA)).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void getListFavorites() throws Exception {
        String nickname = "NickNameTest";
        int page = 1;
        String uri = "/api/v1/users/list/" + nickname + "/" + page;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        assertEquals(200, status);
        assertTrue(content.length() > 0);
    }

    @Test
    public void getFavorites() throws Exception {
        String nickname = "NickNameTest";
        String uri = "/api/v1/users/favorites/" + nickname;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        assertEquals(200, status);
        assertTrue(content.length() > 0);
    }

    @Test
    public void removeFavorite() throws Exception {
        String id = "1";
        String nickname = "NickNameTest";
        String uri = "/api/v1/users/unfavorite";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri).param("recipe", id).param("user", nickname)
                .contentType(MediaType.MULTIPART_FORM_DATA)).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }

}