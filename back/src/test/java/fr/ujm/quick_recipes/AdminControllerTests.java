package fr.ujm.quick_recipes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class AdminControllerTests extends AbstractTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getRecipes() throws Exception {
        int page = 1;
        String uri = "/api/v1/admin/get/" + page;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        assertEquals(200, status);
        assertTrue(content.length() > 0);
    }

}