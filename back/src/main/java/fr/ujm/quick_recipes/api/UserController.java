package fr.ujm.quick_recipes.api;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

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

    @GetMapping(value = "/favorites/{nickname}", produces = { "application/json" })
    public ResponseEntity<MultiValueMap<String, Object>> getFavorites(@PathVariable String nickname) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        User user = userRepo.findUserByNickname(nickname).get();
        body.add("favs", user.getFavorites());
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

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