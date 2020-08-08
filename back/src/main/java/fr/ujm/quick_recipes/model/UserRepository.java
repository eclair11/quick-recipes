package fr.ujm.quick_recipes.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to handle the User entity in the database.
 * 
 * @author Elias Romdan
 */
public interface UserRepository extends JpaRepository<User, Long> {

   Boolean existsByNickname(String nickname);

   Optional<User> findUserByNickname(String nickname);

}