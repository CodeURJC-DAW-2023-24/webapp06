package es.codeurjc.backend.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            User user = userRepository.findByUsername(username).orElseThrow();
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

    public boolean getEqualUserOrAdmin(Principal principal, String username) {
        boolean equalUserOrAdmin = false;
        try {
            String nameUserSesion = principal.getName();
            if (nameUserSesion != null) {
                if (nameUserSesion.equals(username)) {
                    equalUserOrAdmin = true;
                } else {
                    Optional<User> userSesion = userRepository.findByUsername(nameUserSesion);
                    if (userSesion.isPresent() && userSesion.get().getRoles().contains("ADMIN")) {
                        equalUserOrAdmin = true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return equalUserOrAdmin;
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow();
    }

}
