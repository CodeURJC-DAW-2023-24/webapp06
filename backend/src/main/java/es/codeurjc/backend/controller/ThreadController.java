package es.codeurjc.backend.controller;

import java.security.Principal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.service.ForumService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/t")
public class ThreadController {
    @Autowired
    private UserService userService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private ForumService forumService;

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

        /*
        CsrfToken token = request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            model.addAttribute("csrfToken", token.getToken());
        }*/

    }

    @GetMapping("/{threadName}")
    public String getThread(Model model, Principal principal, @PathVariable String threadName) {
        Thread thread = threadService.getThreadByName(threadName);

        class PostWithElapsedTime {
            private Long id;
            private String username;
            private String elapsedTime;
            private String text;
            private boolean hasImage;
            private boolean isLiked;
            private int likes;
            private boolean isDisliked;
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

                this.hasImage = post.getImageFile() != null;

                this.likes = post.getLikes();

                this.dislikes = post.getDislikes();

                String activeUser = null;
                if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
                    activeUser = principal.getName();
                }

                if (activeUser != null) {
                    this.isLiked = post.getUserLikes().contains(userService.getUserByUsername(activeUser));
                    this.isDisliked = post.getUserDislikes().contains(userService.getUserByUsername(activeUser));
                    this.isOwner = (activeUser.equals(this.username)) || userService.isAdmin(principal.getName());
                } else {
                    this.isLiked = false;
                    this.isDisliked = false;
                    this.isOwner = false;
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
        model.addAttribute("forumName", thread.getForum().getName());
        model.addAttribute("isOwnerOrAdmin", isThreadOwner || isAdmin);
        model.addAttribute("posts", postsInfo);
        model.addAttribute("forums", forumService.getAllForums());

        return "thread";
    }

    @GetMapping("/{threadName}/delete")
    public String deleteThread(Model model, Principal principal, @PathVariable String threadName, HttpSession session) {
        Thread thread = threadService.getThreadByName(threadName);
        Forum forum = thread.getForum();
        boolean isAdmin = false;
        boolean isThreadOwner = false;
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            String username = principal.getName();
            isAdmin = userService.isAdmin(username);
            String threadCreator = thread.getCreator().getUsername();
            isThreadOwner = threadCreator.equals(username);
        }
        if (isAdmin || isThreadOwner) {
            forumService.removeThreadFromForum(forum, thread);
            threadService.deleteThread(thread);
        }
        return "redirect:/f/" + forum.getName();
    }

    @PostMapping("/{threadName}/addPost")
    public String addPost(Model model, Principal principal, @RequestParam("post-form-text") String postText,
                            @PathVariable String threadName) throws Exception {
        String name = principal.getName();
        User activeUser = userService.getUserByUsername(name);
        List<User> userLikes = new ArrayList<>();
        List<User> userDislikes = new ArrayList<User>();
        Post newPost = new Post(postText, null, activeUser, userLikes, userDislikes, 0);
        newPost.setText(postText);
        Thread thread = threadService.getThreadByName(threadName);
        List<Post> posts = thread.getPosts();
        posts.add(newPost);
        thread.setPosts(posts);
        return "redirect:/t/" + threadName;

    }

    @PostMapping("/{threadName}/updatePost")
    public String updatePost(Model model, Principal principal, @RequestParam("post-form-text") String postText,
                            @PathVariable String threadName, @PathVariable int postId) {
        Thread thread = threadService.getThreadByName(threadName);
        List<Post> posts = thread.getPosts();
        //No tiene id los posts
        Post updatedPost = posts.get(postId); //aqui deberia encontrar el post
        updatedPost.setText(postText);
        thread.setPosts(posts);
        return "redirect:/t/" + threadName;
    }

    @GetMapping("/{threadName}/deletePost/{postId}")
    public String deletePost(Model model, Principal principal, @PathVariable String threadName, @PathVariable int postId) {
        //No tiene id los posts
        Thread thread = threadService.getThreadByName(threadName);
        List<Post> posts = thread.getPosts();
        posts.remove(postId);
        thread.setPosts(posts);
        return "redirect:/t/" + threadName;
    }
    

}
