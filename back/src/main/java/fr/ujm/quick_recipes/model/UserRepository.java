package fr.ujm.quick_recipes.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

   Boolean existsByNickname(String nickname);

}