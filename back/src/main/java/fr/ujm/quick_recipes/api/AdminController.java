package fr.ujm.quick_recipes.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private static final int PAGE_SIZE = 100;

    private String requestRecipes = "";
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

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

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
        Node current = null;
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeRecipes = doc.getElementsByTagName("recipes").item(0).getChildNodes();
        int nodeRecipesLength = nodeRecipes.getLength();
        System.err.println(nodeRecipes.item(1));
        for (int i = 0; i < nodeRecipesLength; i++) {
            current = nodeRecipes.item(i);
            if (current.getNodeType() == Node.ELEMENT_NODE && current.getNodeName() == "recipe") {
                recipe = new Recipe();
                NodeList nodeRecipe = doc.getElementsByTagName("recipe").item(i).getChildNodes();
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
                            recipe.setAuthor(current.getTextContent());
                            break;
                        case "history":
                            recipe.setHistory(current.getTextContent());
                            break;
                        case "categories":
                            NodeList nodeCategories = doc.getElementsByTagName("categories").item(j).getChildNodes();
                            int nodeCategoriesLength = nodeCategories.getLength();
                            for (int k = 0; k < nodeCategoriesLength; k++) {
                                current = nodeCategories.item(k);
                                if (current.getNodeName() == "category") {
                                    category = new Category();
                                    category.setName(current.getTextContent());
                                    category.setRecipe(recipe);
                                    categories.add(category);
                                }
                            }
                            break;
                        case "pictures":
                            NodeList nodePictures = doc.getElementsByTagName("pictures").item(j).getChildNodes();
                            int nodePicturesLength = nodePictures.getLength();
                            for (int l = 0; l < nodePicturesLength; l++) {
                                current = nodePictures.item(l);
                                if (current.getNodeName() == "picture") {
                                    picture = new Picture();
                                    picture.setName(current.getTextContent());
                                    picture.setRecipe(recipe);
                                    pictures.add(picture);
                                }
                            }
                            break;
                        case "ingredients":
                            NodeList nodeIngredients = doc.getElementsByTagName("ingredients").item(j).getChildNodes();
                            int nodeIngredientsLength = nodeIngredients.getLength();
                            for (int m = 0; m < nodeIngredientsLength; m++) {
                                current = nodeIngredients.item(m);
                                if (current.getNodeName() == "ingredient") {
                                    ingredient = new Ingredient();
                                    ingredient.setName(current.getTextContent());
                                    ingredient.setRecipe(recipe);
                                    ingredients.add(ingredient);
                                }
                            }
                            break;
                        case "preparations":
                            NodeList nodePreparations = doc.getElementsByTagName("preparations").item(j)
                                    .getChildNodes();
                            int nodePreparationsLength = nodePreparations.getLength();
                            for (int n = 0; n < nodePreparationsLength; n++) {
                                current = nodePreparations.item(n);
                                if (current.getNodeName() == "preparation") {
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
    }

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