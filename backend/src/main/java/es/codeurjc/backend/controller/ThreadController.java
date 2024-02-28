package es.codeurjc.backend.controller;

import java.security.Principal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/t")
public class ThreadController {
    @Autowired
    private UserService userService;

    @Autowired
    private ThreadService threadService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            model.addAttribute("logged", true);
            model.addAttribute("username", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/{threadName}")
    public String getThread(Model model, Principal principal, @PathVariable String threadName) {
        Thread thread = threadService.getThreadByName(threadName);

        class PostWithElapsedTime {
            private Long id;
            private String username;
            private String elapsedTime;
            private String text;
            private int likes;
            private int dislikes;
            private boolean isOwner;

            public PostWithElapsedTime(Post post, Principal principal) {
                this.id = post.getId();
                this.username = post.getOwner().getUsername();

                Instant postTime = post.getCreatedAt().toInstant();
                Instant currentTime = Instant.now();
                Duration duration = Duration.between(postTime, currentTime);

                long seconds = duration.getSeconds();
                long days = seconds / (60 * 60 * 24);
                long hours = seconds / (60 * 60);
                long minutes = seconds / 60;

                if (days > 0) {
                    this.elapsedTime = days + " days ago";
                } else if (hours > 0) {
                    this.elapsedTime = hours + " hours ago";
                } else if (minutes > 0) {
                    this.elapsedTime = minutes + " minutes ago";
                } else
                    this.elapsedTime = seconds + " seconds ago";

                this.text = post.getText();

                this.likes = post.getLikes();

                this.dislikes = post.getDislikes();

                this.isOwner = false;
                if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
                    this.isOwner = (principal.getName().equals(this.username))
                            || userService.isAdmin(principal.getName());
                }
            }
        }

        List<PostWithElapsedTime> postsInfo = new ArrayList<>();
        for (Post post : thread.getPosts()) {
            postsInfo.add(new PostWithElapsedTime(post, principal));
        }

        boolean isAdmin = false;
        boolean isThreadOwner = false;
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            String username = principal.getName();
            isAdmin = userService.isAdmin(username);
            String threadCreator = thread.getCreator().getUsername();
            isThreadOwner = threadCreator.equals(username);
        }

        model.addAttribute("threadName", thread.getName());
        model.addAttribute("forumIcon", thread.getForum().getIcon());
        model.addAttribute("isOwnerOrAdmin", isThreadOwner || isAdmin);
        model.addAttribute("posts", postsInfo);

        return "thread";
    }

}
