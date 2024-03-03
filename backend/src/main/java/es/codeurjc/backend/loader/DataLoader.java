package es.codeurjc.backend.loader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import es.codeurjc.backend.repository.*;
import jakarta.annotation.PostConstruct;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.Forum;

@Component
public class DataLoader {

        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ForumRepository forumRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;

        @PostConstruct
        public void run() throws Exception {
                Resource imageUser1 = new ClassPathResource("example/user/user1.png");
                Blob blobImageUser1 = BlobProxy.generateProxy(imageUser1.getInputStream(), imageUser1.contentLength());
                User user1 = new User("username1", "user1@example.com", passwordEncoder.encode("password1"), "USER");
                user1.setImageFile(blobImageUser1);
                User user2 = new User("username2", "user2@example.com", passwordEncoder.encode("password2"), "USER",
                                "ADMIN");
                User user3 = new User("CyberKnight", "cyberknight@example.com", passwordEncoder.encode("securePass3"),
                                "USER");
                User user4 = new User("PhantomHacker", "phantomhacker@example.com",
                                passwordEncoder.encode("securePass4"),
                                "USER");
                User user5 = new User("VirtualMage", "virtualmage@example.com", passwordEncoder.encode("securePass5"),
                                "USER");
                User user6 = new User("PixelPirate", "pixelpirate@example.com", passwordEncoder.encode("securePass6"),
                                "USER");
                User user7 = new User("QuantumSorcerer", "quantumsorcerer@example.com",
                                passwordEncoder.encode("securePass7"),
                                "USER");
                User user8 = new User("RetroRaider", "retroraider@example.com", passwordEncoder.encode("securePass8"),
                                "USER");
                User user9 = new User("ShadowMercenary", "shadowmercenary@example.com",
                                passwordEncoder.encode("securePass9"),
                                "USER");
                User user10 = new User("GalacticGuardian", "galacticguardian@example.com",
                                passwordEncoder.encode("securePass10"), "USER");
                User user11 = new User("NinjaPixel", "ninjapixel@example.com", passwordEncoder.encode("securePass1"),
                                "USER");
                User user12 = new User("DragonRider", "dragonrider@example.com", passwordEncoder.encode("securePass2"),
                                "USER");
                User user13 = new User("delete", "delete@example.com", passwordEncoder.encode("securePass2"), "USER");
                List<User> users = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10,
                                user11, user12, user13);
                for (User i : users) {
                        i.setIsActive(true);
                }
                userRepository.saveAll(users);
                List<User> userLikes = Arrays.asList(user4, user5, user6, user7, user8, user9);
                List<User> userDislikes = Arrays.asList(user10, user11, user12, user13);
                // ... guardar más usuarios si has creado más

                // O si prefieres guardarlos todos a la vez
                // userRepository.saveAll(Arrays.asList(user1, user2, ...));

                // String imagePath = "/ruta/a/imagen.jpg";
                // Blob imageBlob = imageService.loadImage(imagePath);
                Forum forum1 = new Forum("Books", null, "book");
                Forum forum2 = new Forum("Technology", null, "laptop-code");
                Forum forum3 = new Forum("Science", null, "flask");
                Forum forum4 = new Forum("Sports", null, "football-ball");
                Forum forum5 = new Forum("Music", null, "music");
                Forum forum6 = new Forum("Movies", null, "film");
                Forum forum7 = new Forum("Gastronomy", null, "utensils");
                Forum forum8 = new Forum("Travel", null, "plane");
                Forum forum9 = new Forum("Gaming", null, "gamepad");

                Thread thread1 = new Thread("Saludos", forum1, new ArrayList<Post>(), user1);
                Thread thread2 = new Thread("Pruebas", forum2, new ArrayList<Post>(), user2);

                Resource imagePost1 = new ClassPathResource("example/post/foto_post.jpg");
                Blob blobImagePost1 = BlobProxy.generateProxy(imagePost1.getInputStream(), imagePost1.contentLength());

                Post post1 = new Post("hola", blobImagePost1, user1, thread1, userLikes, userDislikes, 1);
                Post post2 = new Post("adios", null, user2, thread1, userLikes, userDislikes, 3);
                Post post3 = new Post(
                                "In the quiet town of Neo-Silica, a revolution brews under the neon lights.", null,
                                user1, thread1, userLikes, userDislikes, 1);

                Post post4 = new Post(
                                "Far beyond the reach of civilization, the ancient forest of Eldergrove whispers secrets of a bygone era.",
                                null, user2, thread1, userLikes, userDislikes, 2);

                Post post5 = new Post(
                                "Galactic Odyssey: A Journey Beyond Stars", null, user3, thread2, userLikes,
                                userDislikes, 3);

                Post post6 = new Post(
                                "The Last Codebender", null, user4, thread2, userLikes, userDislikes, 4);

                Post post7 = new Post(
                                "Whispers in the Digital Wind", null, user5, thread2, userLikes, userDislikes, 5);

                // postRepository.saveAll(Arrays.asList(post1, post2));
                //

                // threadRepository.save(thread1);

                thread1.setPosts(Arrays.asList(post1, post2, post3, post4));
                thread2.setPosts(Arrays.asList(post5, post6, post7));

                forum1.setThreads(Arrays.asList(thread1));
                forum2.setThreads(Arrays.asList(thread2));
                forumRepository.save(forum1);
                forumRepository.save(forum2);
                forumRepository.save(forum3);
                forumRepository.save(forum4);
                forumRepository.save(forum5);
                forumRepository.save(forum6);
                forumRepository.save(forum7);
                forumRepository.save(forum8);
                forumRepository.save(forum9);
        }
}
