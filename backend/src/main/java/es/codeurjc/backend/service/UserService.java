package es.codeurjc.backend.service;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.codeurjc.backend.exceptions.UserNotFoundException;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.PostRepository;
import es.codeurjc.backend.repository.ThreadRepository;
import es.codeurjc.backend.repository.UserRepository;
import es.codeurjc.backend.security.RepositoryUserDetailsService;
import org.springframework.security.core.Authentication;

@Service
public class UserService {

    @Autowired
    private RepositoryUserDetailsService userDetailsService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByname(String username) {
        return userRepository.findByUsername(username);
    }
    

    public void save(User user){
        userRepository.save(user);
    }
    
    public Boolean deleteUser(String username) {
        try {
            User user = getUserByUsername(username);
            User deleteUser = userRepository.findByUsername("delete").orElseThrow();
            threadRepository.changeOwnerOfThreads(user.getId(), deleteUser.getId());
            postRepository.changeOwnerOfPosts(user.getId(), deleteUser.getId());
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
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found with username: " + username + ".");
        }
    }

    public Optional<User> getUserByUsernameNoException(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> getUsersByUsernameStartingWith(String prefix, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByUsernameStartingWith(prefix, pageable);
    }

    public Boolean isAdmin(String username) {
        User userSesion = getUserByUsername(username);
        return userSesion.getRoles().contains("ADMIN");
    }

    public void update(User user) {
        userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Page<User> getUsersPaginated(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public void createUser(String username, String email, String password) {
        try {
            User user = new User(username, email, passwordEncoder.encode(password), "USER");
            userRepository.save(user);
            emailService.sendActivation(email, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
