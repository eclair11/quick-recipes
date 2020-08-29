package fr.ujm.quick_recipes.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.ujm.quick_recipes.model.Category;
import fr.ujm.quick_recipes.model.CategoryRepository;
import fr.ujm.quick_recipes.model.Ingredient;
import fr.ujm.quick_recipes.model.IngredientRepository;
import fr.ujm.quick_recipes.model.Picture;
import fr.ujm.quick_recipes.model.PictureRepository;
import fr.ujm.quick_recipes.model.Preparation;
import fr.ujm.quick_recipes.model.PreparationRepository;
import fr.ujm.quick_recipes.model.Recipe;
import fr.ujm.quick_recipes.model.RecipeRepository;

/**
 * REST controller to handle API in relation with the admin tasks.
 * 
 * @author Elias Romdan
 */
@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    /* Set the maximum number of recipes displayed in one page */
    private static final int PAGE_SIZE = 100;

    /* Set the path where files are saved */
    private static final String PATH_FILES = "src/main/resources/static/xml/";

    /* Set the path where pictures are saved */
    private static final String PATH_UPLOAD = "src/main/resources/static/img/";

    /* Set the path to access the pictures */
    private static final String PATH_ACCESS = "http://localhost:8080/img/";

    /* Object to handle SQL request to find recipes */
    private String requestRecipes = "";

    /* Object to handle SQL request to find the number of recipes */
    private String requestPages = "";

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CategoryRepository catRepo;

    @Autowired
    IngredientRepository ingRepo;

    @Autowired
    PictureRepository picRepo;

    @Autowired
    PreparationRepository preRepo;

    @Autowired
    RecipeRepository recRepo;

    /**
     * Recieve a list of XML files as parameters, check them, then pass them to the
     * parser function.
     * 
     * @param files list of files to parse
     * @return ResponseEntity<String>
     */
    @PostMapping(value = "/add", consumes = { "multipart/form-data" }, produces = { "application/json" })
    public ResponseEntity<String> addRecipes(@RequestParam(name = "files") MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                parser(convert(file));
            }
        } catch (SAXException e) {
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IOException e) {
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ParserConfigurationException e) {
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * Convert a file object from MultipartFile to File.
     * 
     * @param file the file to convert
     * @return File
     * @throws IOException
     */
    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(PATH_FILES + file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /**
     * Parse the content of the file into objects, then save them in the database.
     * 
     * @param file the converted file ready to parse
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private void parser(File file) throws SAXException, IOException, ParserConfigurationException {
        List<Recipe> recipes = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Picture> pictures = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Preparation> preparations = new ArrayList<>();
        Recipe recipe = null;
        Category category = null;
        Picture picture = null;
        Ingredient ingredient = null;
        Preparation preparation = null;
        boolean first = true;
        Node current = null;
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeRecipes = doc.getElementsByTagName("recipes").item(0).getChildNodes();
        int nodeRecipesLength = nodeRecipes.getLength();
        for (int i = 0; i < nodeRecipesLength; i++) {
            current = nodeRecipes.item(i);
            if (current.getNodeType() == Node.ELEMENT_NODE && current.getNodeName() == "recipe") {
                recipe = new Recipe();
                NodeList nodeRecipe = current.getChildNodes();
                int nodeRecipeLength = nodeRecipe.getLength();
                for (int j = 0; j < nodeRecipeLength; j++) {
                    current = nodeRecipe.item(j);
                    switch (current.getNodeName()) {
                        case "name":
                            recipe.setName(current.getTextContent());
                            break;
                        case "region":
                            recipe.setRegion(current.getTextContent());
                            break;
                        case "discovery":
                            recipe.setDiscovery(current.getTextContent());
                            break;
                        case "author":
                            recipe.setAuthor(current.getTextContent());
                            break;
                        case "calorie":
                            recipe.setCalorie(current.getTextContent());
                            break;
                        case "history":
                            recipe.setHistory(current.getTextContent());
                            break;
                        case "category":
                            NodeList nodeCategories = current.getChildNodes();
                            int nodeCategoriesLength = nodeCategories.getLength();
                            for (int k = 0; k < nodeCategoriesLength; k++) {
                                current = nodeCategories.item(k);
                                if (current.getNodeName() == "cat") {
                                    category = new Category();
                                    category.setName(current.getTextContent());
                                    category.setRecipe(recipe);
                                    categories.add(category);
                                }
                            }
                            break;
                        case "picture":
                            NodeList nodePictures = current.getChildNodes();
                            int nodePicturesLength = nodePictures.getLength();
                            for (int k = 0; k < nodePicturesLength; k++) {
                                current = nodePictures.item(k);
                                if (current.getNodeName() == "pic") {
                                    String path = download(current.getTextContent());
                                    picture = new Picture();
                                    picture.setName(path);
                                    picture.setRecipe(recipe);
                                    pictures.add(picture);
                                    if (first) {
                                        recipe.setPicture(picture.getName());
                                        first = false;
                                    }
                                }
                            }
                            first = true;
                            break;
                        case "ingredient":
                            NodeList nodeIngredients = current.getChildNodes();
                            int nodeIngredientsLength = nodeIngredients.getLength();
                            for (int k = 0; k < nodeIngredientsLength; k++) {
                                current = nodeIngredients.item(k);
                                if (current.getNodeName() == "ing") {
                                    ingredient = new Ingredient();
                                    ingredient.setName(current.getTextContent());
                                    ingredient.setRecipe(recipe);
                                    ingredients.add(ingredient);
                                }
                            }
                            break;
                        case "preparation":
                            NodeList nodePreparations = current.getChildNodes();
                            int nodePreparationsLength = nodePreparations.getLength();
                            for (int k = 0; k < nodePreparationsLength; k++) {
                                current = nodePreparations.item(k);
                                if (current.getNodeName() == "pre") {
                                    preparation = new Preparation();
                                    preparation.setName(current.getTextContent());
                                    preparation.setRecipe(recipe);
                                    preparations.add(preparation);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
                recipes.add(recipe);
            }
        }
        recRepo.saveAll(recipes);
        catRepo.saveAll(categories);
        picRepo.saveAll(pictures);
        ingRepo.saveAll(ingredients);
        preRepo.saveAll(preparations);
    }

    /**
     * Save pictures into the server from their link.
     * 
     * @param link
     * @throws IOException
     * @return String
     */
    private String download(String link) throws IOException {
        String name = link.substring(link.lastIndexOf("/") + 1);
        try {
            URL url = new URL("file:///" + link);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(PATH_UPLOAD + name);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            System.err.println(e);
        }
        return PATH_ACCESS + name;
    }

    /**
     * Get the list of all the recipes found in the database.
     * 
     * @param page number of page
     * @return ResponseEntity<MultiValueMap<String, Object>>
     */
    @GetMapping(value = "/get/{page}", produces = { "application/json" })
    public ResponseEntity<MultiValueMap<String, Object>> getRecipes(@PathVariable int page) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        this.requestRecipes = "Select r.id, r.name From Recipe r";
        this.requestPages = "Select count(r.id) From Recipe r";
        Query queryRecipes = entityManager.createQuery(this.requestRecipes);
        Query queryPages = entityManager.createQuery(this.requestPages);
        queryRecipes.setFirstResult((page - 1) * PAGE_SIZE);
        queryRecipes.setMaxResults(PAGE_SIZE);
        List<Object> recipes = RecipeController.castList(Object.class, queryRecipes.getResultList());
        int pages = (Integer.valueOf(queryPages.getSingleResult().toString()) + PAGE_SIZE - 1) / PAGE_SIZE;
        body.add("recipes", recipes);
        body.add("pages", pages);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    /**
     * Remove the recipes from the database using their identifiers passed as
     * parameters.
     * 
     * @param removes list of recipes identifiers
     * @return ResponseEntity<String>
     */
    @Transactional
    @PutMapping(value = "/delete/{removes}", produces = { "application/json" })
    public ResponseEntity<String> removeRecipes(@PathVariable Long[] removes) {
        for (Long id : removes) {
            Recipe r = recRepo.findById(id).get();
            catRepo.deleteByRecipe(r);
            ingRepo.deleteByRecipe(r);
            picRepo.deleteByRecipe(r);
            preRepo.deleteByRecipe(r);
            recRepo.delete(r);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}