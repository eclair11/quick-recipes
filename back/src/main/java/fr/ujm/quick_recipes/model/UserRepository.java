package fr.ujm.quick_recipes.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

   Boolean existsByNickname(String nickname);
   Optional<User> findUserByNickname(String nickname);

}