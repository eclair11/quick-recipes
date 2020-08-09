package fr.ujm.quick_recipes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class RecipeControllerTests extends AbstractTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getListCategories() throws Exception {
        String uri = "/api/v1/recipes/categories/";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        assertEquals(200, status);
        assertTrue(content.length() > 0);
    }

    @Test
    public void getListRegions() throws Exception {
        String uri = "/api/v1/recipes/regions/";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        assertEquals(200, status);
        assertTrue(content.length() > 0);
    }

    @Test
    public void getListRecipesNormal() throws Exception {
        String type = "normal";
        String key = "salade";
        int page = 1;
        String uri = "/api/v1/recipes/list/" + type + "/" + key + "/" + page;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        assertEquals(200, status);
        assertTrue(content.length() > 0);
    }

    @Test
    public void getListRecipesSpecial() throws Exception {
        String type = "special";
        String key = "eau, sucre, orange";
        int page = 1;
        String uri = "/api/v1/recipes/list/" + type + "/" + key + "/" + page;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        assertEquals(200, status);
        assertTrue(content.length() > 0);
    }

    @Test
    public void getListRecipesCategory() throws Exception {
        String type = "category";
        String key = "Sucré";
        int page = 1;
        String uri = "/api/v1/recipes/list/" + type + "/" + key + "/" + page;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        assertEquals(200, status);
        assertTrue(content.length() > 0);
    }

    @Test
    public void getListRecipesRegion() throws Exception {
        String type = "region";
        String key = "Italienne";
        int page = 1;
        String uri = "/api/v1/recipes/list/" + type + "/" + key + "/" + page;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        assertEquals(200, status);
        assertTrue(content.length() > 0);
    }

    @Test
    public void getRecipe() throws Exception {
        Long id = Long.valueOf(1);
        String uri = "/api/v1/recipes/" + id;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        assertEquals(200, status);
        assertTrue(content.length() > 0);
    }

}