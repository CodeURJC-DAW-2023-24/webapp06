package es.codeurjc.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.repository.PostRepository;
import es.codeurjc.backend.repository.ThreadRepository;
import es.codeurjc.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/profile/{username}")
    public String UserProfile(Model model, Principal principal, @PathVariable String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        List<Thread> threads = threadRepository.findByOwner(user).orElseThrow();
        List<Post> post = postRepository.findByOwner(user).orElseThrow();
        boolean equalUserOrAdmin = getEqualUserOrAdmin(principal, username);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("numberPosts", post.size());
        model.addAttribute("numberThreads", threads.size());
        model.addAttribute("threads", threads);
        model.addAttribute("equalUserOrAdmin", equalUserOrAdmin);

        for (Thread thread : threads) {
            thread.setNumberPosts();
        }

        return "profile";
    }

    private boolean getEqualUserOrAdmin(Principal principal, String username) {
        boolean equalUserOrAdmin = false;
        try{
            String nameUserSesion = principal.getName();
            nameUserSesion = principal.getName();
            if(nameUserSesion != null){
                if (nameUserSesion.equals(username)) {
                    equalUserOrAdmin = true;
                 }
                 else{
                    Optional<User> userSesion = userRepository.findByUsername(nameUserSesion);
                    if(userSesion.isPresent() && userSesion.get().getRoles().contains("ADMIN")){
                        equalUserOrAdmin = true;
                    }
                 }
            }
        }
        catch(Exception e){}
        return equalUserOrAdmin;
    }

    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    


}
