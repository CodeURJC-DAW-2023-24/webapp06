package es.codeurjc.backend.restcontroller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.UserService;

@RestController
@RequestMapping("/p")
public class PostRestController {

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    @GetMapping("/like/{postId}")
    public boolean likePost(Model model, Principal principal, @PathVariable String postId) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            if (postService.addPostLike(Long.parseLong(postId), user)) {
                postService.removePostDislike(Long.parseLong(postId), user);
            } else {
                postService.removePostLike(Long.parseLong(postId), user);
            }
            return true;
        }
        return false;
    }

    @GetMapping("/dislike/{postId}")
    public boolean dislikePost(Model model, Principal principal, @PathVariable String postId) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            if (postService.addPostDislike(Long.parseLong(postId), user)) {
                postService.removePostLike(Long.parseLong(postId), user);
            } else {
                postService.removePostDislike(Long.parseLong(postId), user);
            }
            return true;
        }
        return false;
    }
}
