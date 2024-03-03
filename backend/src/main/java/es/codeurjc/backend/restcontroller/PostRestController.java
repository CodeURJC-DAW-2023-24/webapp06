package es.codeurjc.backend.restcontroller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.UserService;
import es.codeurjc.backend.service.ThreadService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/p")
public class PostRestController {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    ThreadService threadService;

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

    @GetMapping("/delete/{postId}/{threadName}")
    public boolean deletePost(Model model, Principal principal, @PathVariable String postId, @PathVariable String threadName) {
        Thread thread = threadService.getThreadByName(threadName);
        threadService.deletePostFromThread(thread, Long.parseLong(postId));
        return true;
    }

    @GetMapping("/report/{postId}")
    public boolean getMethodName(Model model, Principal principal, @PathVariable String postId) {
        postService.reportPost(Long.parseLong(postId));
        return true;
    }
    

    @GetMapping("/validate/{postId}")
    public void validatePost(Model model, Principal principal, @PathVariable String postId) {
        postService.validatePost(Long.parseLong(postId));
    }

    @GetMapping("/reports")
    public Page<Post> getReportedPaginated(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return postService.getReportedPosts(page, size);
    }

    @GetMapping("/threads/{threadId}/posts")
    public Page<Post> getPostsByThread(@PathVariable Long threadId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return postService.getPostsByThread(threadId, page, size);
    }

}
