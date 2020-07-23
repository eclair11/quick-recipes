package fr.ujm.quick_recipes.config;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.ujm.quick_recipes.model.User;
import fr.ujm.quick_recipes.model.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository UserRepo;

    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Optional<User> user = UserRepo.findUserByNickname(nickname);
        if (user == null) {
            throw new UsernameNotFoundException(nickname + " non trouvé");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getNickname(),
                user.get().getPassword(), new ArrayList<>());
    }

}