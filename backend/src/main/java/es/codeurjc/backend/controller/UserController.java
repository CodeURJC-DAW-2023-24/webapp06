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
import org.springframework.stereotype.Controller;
import java.util.List;

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
    public String UserProfile(Model model, @PathVariable String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        List<Thread> threads = threadRepository.findByOwner(user).orElseThrow();
        List<Post> post = postRepository.findByOwner(user).orElseThrow();

        model.addAttribute("username", user.getUsername());
        model.addAttribute("numberPosts", post.size());
        model.addAttribute("numberThreads", threads.size());
        model.addAttribute("threads", threads);

        for (Thread thread : threads) {
            thread.setNumberPosts();
        }

        return "profile";
    }

}
