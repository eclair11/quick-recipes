package fr.ujm.quick_recipes.api;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.ujm.quick_recipes.model.User;
import fr.ujm.quick_recipes.model.UserRepository;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRepository userRepo;

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
        u.setPassword(password);
        userRepo.save(u);
        body.add("message", "Compte d'utilisateur créé avec succès");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping(value = "/signin", produces = { "application/json" })
    public ResponseEntity<String> signIn() {
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PostMapping(value = "/signout", produces = { "application/json" })
    public ResponseEntity<String> signOut() {
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

}