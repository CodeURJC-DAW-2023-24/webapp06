package es.codeurjc.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.UserService;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ThreadService threadService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping("/profile/{username}")
    public String UserProfile(Model model, Principal principal, @PathVariable String username) {
        User user = userService.getUserByUsername(username);
        List<Thread> threads = threadService.getThreadsByOwner(user);
        List<Post> post = postService.getPostByOwner(user);
        boolean equalUserOrAdmin = userService.getEqualUserOrAdmin(principal, username);

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

    @GetMapping("/delete/{username}")
    public String DeleteProfile(Model model, Principal principal, @PathVariable String username) {
        boolean equalUserOrAdmin = userService.getEqualUserOrAdmin(principal, username);
        if (equalUserOrAdmin) {
            userService.deleteUser(username);
        }
        return "redirect:/";
    }

    @GetMapping("/users")
    public String Users(Model model, Principal principal,
            @RequestParam(value = "username", required = false) String username) {
        List<User> users;
        if (username != null && !username.isEmpty()) {
            users = userService.getUsersByUsernameStartingWith(username);
        } else {
            users = userService.getAllUsers();
        }
        model.addAttribute("users", users);
        return "users_template";
    }
}
