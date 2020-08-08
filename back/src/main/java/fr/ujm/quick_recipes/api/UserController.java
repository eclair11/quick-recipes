package fr.ujm.quick_recipes.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.ujm.quick_recipes.config.JwtTokenUtil;
import fr.ujm.quick_recipes.model.JwtRequest;
import fr.ujm.quick_recipes.model.User;
import fr.ujm.quick_recipes.model.UserRepository;

/**
 * REST controller to handle API in relation with the User object.
 * 
 * @author Elias Romdan
 */
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    /* Set the maximum number of recipes displayed in one page */
    private static final int PAGE_SIZE = 100;

    /* Object to handle SQL request to find recipes */
    private String requestRecipes = "";

    /* Object to handle SQL request to find the number of recipes */
    private String requestPages = "";

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepo;

    /**
     * Subscribe a user into the database using its nickname and password.
     * 
     * @param user User object
     * @return ResponseEntity<MultiValueMap<String, String>>
     */
    @PostMapping(value = "/signup", produces = { "application/json" })
    public ResponseEntity<MultiValueMap<String, String>> signUp(@RequestBody User user) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String nickname = user.getNickname();
        String password = user.getPassword();
        if (userRepo.existsByNickname(nickname)) {
            body.add("message", "Nom d'utilisateur existe déjà");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
        User u = new User();
        u.setNickname(nickname);
        u.setPassword(encoder.encode(password));
        userRepo.save(u);
        body.add("message", "Compte d'utilisateur créé avec succès");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    /**
     * Connect a user to its account based on its nickname and password.
     * 
     * @param request User object
     * @return ResponseEntity<MultiValueMap<String, Object>>
     */
    @PostMapping(value = "/signin", produces = { "application/json" })
    public ResponseEntity<MultiValueMap<String, Object>> signIn(@RequestBody JwtRequest request) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        String nickname = request.getNickname();
        String password = request.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nickname, password));
        } catch (DisabledException e) {
            body.add("token", "Utilisateur désactivé");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        } catch (BadCredentialsException e) {
            body.add("token", "Pseudo ou mot de passe incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(nickname);
        body.add("token", jwtTokenUtil.generateToken(userDetails));
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    /**
     * Send the list of the recipes put in the favorite by the user.
     * 
     * @param nickname user nickname
     * @param page number of page
     * @return ResponseEntity<MultiValueMap<String, Object>>
     */
    @GetMapping(value = "/list/{nickname}/{page}", produces = { "application/json" })
    public ResponseEntity<MultiValueMap<String, Object>> getListFavorites(@PathVariable String nickname,
            @PathVariable int page) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        User user = userRepo.findUserByNickname(nickname).get();
        if (user.getFavorites().size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String favs = user.getFavorites().toString().replace("[", "(").replace("]", ")");
        this.requestRecipes = "Select r.id, r.name From Recipe r Where r.id In " + favs;
        this.requestPages = "Select count(r.id) From Recipe r Where r.id In " + favs;
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
     * Send the list of the recipes identifiers put in the favorite by the user.
     * 
     * @param nickname user nickname
     * @return ResponseEntity<MultiValueMap<String, Object>>
     */
    @GetMapping(value = "/favorites/{nickname}", produces = { "application/json" })
    public ResponseEntity<MultiValueMap<String, Object>> getFavorites(@PathVariable String nickname) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        User user = userRepo.findUserByNickname(nickname).get();
        body.add("favs", user.getFavorites());
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    /**
     * Add a recipe in the user favorites based on its identifier.
     * 
     * @param id recipe identifier
     * @param nickname user nickname
     * @return ResponseEntity<String>
     */
    @PutMapping(value = "/favorite", consumes = { "multipart/form-data" }, produces = { "application/json" })
    public ResponseEntity<String> addFavorite(@RequestParam(name = "recipe") Long id,
            @RequestParam(name = "user") String nickname) {
        User user = userRepo.findUserByNickname(nickname).get();
        List<Long> favs = user.getFavorites();
        favs.add(id);
        user.setFavorites(favs);
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * Remove a recipe from the user favorties based on its identifier.
     * 
     * @param id recipe identifier
     * @param nickname user nickname
     * @return ResponseEntity<String>
     */
    @Transactional
    @PutMapping(value = "/unfavorite", consumes = { "multipart/form-data" }, produces = { "application/json" })
    public ResponseEntity<String> removeFavorite(@RequestParam(name = "recipe") Long id,
            @RequestParam(name = "user") String nickname) {
        User user = userRepo.findUserByNickname(nickname).get();
        List<Long> favs = user.getFavorites();
        favs.remove(id);
        user.setFavorites(favs);
        userRepo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}