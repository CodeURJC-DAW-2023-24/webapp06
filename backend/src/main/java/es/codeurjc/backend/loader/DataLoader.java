package es.codeurjc.backend.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import es.codeurjc.backend.repository.UserRepository;
import es.codeurjc.backend.model.User;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        User user1 = new User("username1", "user1@example.com", passwordEncoder.encode("password1"), "USER");
        User user2 = new User("username2", "user2@example.com", passwordEncoder.encode("password2"), "USER", "ADMIN");
       
        userRepository.saveAll(Arrays.asList(user1, user2));
        // ... guardar más usuarios si has creado más

        // O si prefieres guardarlos todos a la vez
        // userRepository.saveAll(Arrays.asList(user1, user2, ...));
    }
}
