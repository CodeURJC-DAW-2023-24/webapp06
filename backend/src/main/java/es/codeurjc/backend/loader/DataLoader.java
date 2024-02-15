package es.codeurjc.backend.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import es.codeurjc.backend.repository.*;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.Forum;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
/*
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ThreadRepository threadRepository;
*/
    @Autowired
    private ForumRepository forumRepository;

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
        
        Post post1 = new Post("hola", null, user1, 20, 10, 1);
        Post post2 = new Post("adios", null, user2, 10, 20, 3);

        //postRepository.saveAll(Arrays.asList(post1, post2));

        Thread thread1 = new Thread("Saludos", Arrays.asList(post1, post2), user1);

        //threadRepository.save(thread1);

        Forum forum1 = new Forum("Libros", Arrays.asList(thread1), "test");

        forumRepository.save(forum1);
    }
}
