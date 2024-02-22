package es.codeurjc.backend.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.repository.PostRepository;
import es.codeurjc.backend.repository.ThreadRepository;
import es.codeurjc.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private PostRepository postRepository;

    public Boolean deleteUser(String username) {
        try {
            User user = getUserByUsername(username);
            User deleteUser = userRepository.findByUsername("delete").orElseThrow();
            List<Thread> threads = threadRepository.findByOwner(user).orElseThrow();
            List<Post> posts = postRepository.findByOwner(user).orElseThrow();
            for (Thread i : threads) {
                i.setCreator(deleteUser);
                threadRepository.save(i);
            }
            for (Post i : posts) {
                i.setOwner(deleteUser);
                postRepository.save(i);
            }
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getEqualUserOrAdmin(String usernameLogin, String username) {
        boolean equalUserOrAdmin = false;
        try {
            if (usernameLogin != null) {
                if (usernameLogin.equals(username)) {
                    equalUserOrAdmin = true;
                } else {
                    Optional<User> userSesion = userRepository.findByUsername(usernameLogin);
                    if (userSesion.isPresent() && userSesion.get().getRoles().contains("ADMIN")) {
                        equalUserOrAdmin = true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return equalUserOrAdmin;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByUsernameStartingWith(String prefix) {
        return userRepository.findByUsernameStartingWith(prefix);
    }

    public Boolean isAdmin(String username) {
        User userSesion = getUserByUsername(username);
        return userSesion.getRoles().contains("ADMIN");
    }

    public void update(User user) {
        userRepository.save(user);
    }
}
