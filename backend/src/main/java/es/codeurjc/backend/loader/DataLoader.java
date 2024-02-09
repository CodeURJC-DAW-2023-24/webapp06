package es.codeurjc.backend.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import es.codeurjc.backend.repository.UserRepository;
import es.codeurjc.backend.model.User;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        
        User user1 = new User("username1", "user1@example.com", "password1");
        User user2 = new User("username2", "user2@example.com", "password2");
       
        userRepository.saveAll(Arrays.asList(user1, user2));
        // ... guardar más usuarios si has creado más

        // O si prefieres guardarlos todos a la vez
        // userRepository.saveAll(Arrays.asList(user1, user2, ...));
    }
}
