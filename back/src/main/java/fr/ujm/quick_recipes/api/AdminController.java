package fr.ujm.quick_recipes.api;

import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @PostMapping(value = "/add", produces = { "application/json" })
    public ResponseEntity<MultiValueMap<String, String>> addRecipes(@RequestBody File[] files) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

}