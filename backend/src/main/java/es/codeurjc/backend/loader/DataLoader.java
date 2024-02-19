package es.codeurjc.backend.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.sql.Blob;

import java.util.Arrays;

import org.hibernate.engine.jdbc.BlobProxy;
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
     * @Autowired
     * private PostRepository postRepository;
     * 
     * @Autowired
     * private ThreadRepository threadRepository;
     */
    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        Resource imageUser1 = new ClassPathResource("example/user/user1.png");
        Blob blobImageUser1 = BlobProxy.generateProxy(imageUser1.getInputStream(), imageUser1.contentLength());

        User user1 = new User("username1", "user1@example.com", passwordEncoder.encode("password1"), "USER");
        user1.setImageFile(blobImageUser1);
        User user2 = new User("username2", "user2@example.com", passwordEncoder.encode("password2"), "USER", "ADMIN");
        // User 3
        User user3 = new User("CyberKnight", "cyberknight@example.com", passwordEncoder.encode("securePass3"), "USER");
        User user4 = new User("PhantomHacker", "phantomhacker@example.com", passwordEncoder.encode("securePass4"),
                "USER");
        User user5 = new User("VirtualMage", "virtualmage@example.com", passwordEncoder.encode("securePass5"), "USER");
        User user6 = new User("PixelPirate", "pixelpirate@example.com", passwordEncoder.encode("securePass6"), "USER");
        User user7 = new User("QuantumSorcerer", "quantumsorcerer@example.com", passwordEncoder.encode("securePass7"),
                "USER");
        User user8 = new User("RetroRaider", "retroraider@example.com", passwordEncoder.encode("securePass8"), "USER");
        User user9 = new User("ShadowMercenary", "shadowmercenary@example.com", passwordEncoder.encode("securePass9"),
                "USER");
        User user10 = new User("GalacticGuardian", "galacticguardian@example.com",
                passwordEncoder.encode("securePass10"), "USER");
        User user11 = new User("NinjaPixel", "ninjapixel@example.com", passwordEncoder.encode("securePass1"), "USER");
        User user12 = new User("DragonRider", "dragonrider@example.com", passwordEncoder.encode("securePass2"), "USER");
        User user13 = new User("delete", "delete@example.com", passwordEncoder.encode("securePass2"), "USER");

        userRepository.saveAll(
                Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11, user12, user13));
        // ... guardar más usuarios si has creado más

        // O si prefieres guardarlos todos a la vez
        // userRepository.saveAll(Arrays.asList(user1, user2, ...));

        // String imagePath = "/ruta/a/imagen.jpg";
        // Blob imageBlob = imageService.loadImage(imagePath);

        Post post1 = new Post("hola", null, user1, 20, 10, 1);
        Post post2 = new Post("adios", null, user2, 10, 20, 3);
        Post post3 = new Post(
                "In the quiet town of Neo-Silica, a revolution brews under the neon lights.",
                null,
                user1,
                20, 10, 1);

        Post post4 = new Post(
                "Far beyond the reach of civilization, the ancient forest of Eldergrove whispers secrets of a bygone era.",
                null,
                user2,
                35, 5, 2);

        Post post5 = new Post(
                "Galactic Odyssey: A Journey Beyond Stars",
                null,
                user3,
                45, 15, 3);

        Post post6 = new Post(
                "The Last Codebender",
                null,
                user4,
                50, 20, 4);

        Post post7 = new Post(
                "Whispers in the Digital Wind",
                null,
                user5,
                60, 25, 5);

        // postRepository.saveAll(Arrays.asList(post1, post2));
        // 
        Thread thread1 = new Thread("Saludos", Arrays.asList(post1, post2, post3, post4), user1);
        Thread thread2 = new Thread("Pruebas", Arrays.asList(post5, post6, post7), user1);
        //threadRepository.save(thread1);

        Forum forum1 = new Forum("Libros", Arrays.asList(thread1, thread2), "test");

        forumRepository.save(forum1);
    }
}
