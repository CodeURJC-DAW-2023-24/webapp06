package es.codeurjc.backend.controller;

import java.security.Principal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.dto.PostTimeDTO;
import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.service.ForumService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/t")
public class ThreadController {
    @Autowired
    private UserService userService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private PostService postService;

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

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());

    }

    @GetMapping("/{threadName}")
    public String getThread(Model model, Principal principal, @PathVariable String threadName) {
        Thread thread = threadService.getThreadByName(threadName);

        User activeUser = null;
        boolean isAdmin = false;
        boolean isThreadOwner = false;
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            String username = principal.getName();
            activeUser = userService.getUserByUsername(username);
            isAdmin = userService.isAdmin(username);
            String threadCreator = thread.getOwner().getUsername();
            isThreadOwner = threadCreator.equals(username);
        }

        List<PostTimeDTO> postsInfo = new ArrayList<>();
        for (Post post : thread.getPosts()) {
            postsInfo.add(new PostTimeDTO(post, activeUser, isAdmin));
        }

        model.addAttribute("threadName", thread.getName());
        model.addAttribute("forumIcon", thread.getForum().getIcon());
        model.addAttribute("forumName", thread.getForum().getName());
        model.addAttribute("isOwnerOrAdmin", isThreadOwner || isAdmin);
        model.addAttribute("posts", postsInfo);
        model.addAttribute("forums", forumService.getAllForums());

        return "thread_template";
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
            String threadCreator = thread.getOwner().getUsername();
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
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            @PathVariable String threadName) throws Exception {
        String name = principal.getName();
        User activeUser = userService.getUserByUsername(name);
        List<User> userLikes = new ArrayList<>();
        List<User> userDislikes = new ArrayList<User>();
        Thread thread = threadService.getThreadByName(threadName);
        Blob image = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                image = BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize());
            } catch (Exception e) {
                return "error_template";
            }
        }
        Post newPost = new Post(postText, image, activeUser, thread, userLikes, userDislikes, 0);
        threadService.addPostToThread(thread, newPost);
        return "redirect:/t/" + threadName;

    }

    @PostMapping("/{threadName}/updatePost")
    public String updatePost(Model model, Principal principal, @RequestParam("post-form-text") String postText,
            @PathVariable String threadName, @RequestParam("copiaId") Long postId,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile) {
        Post updatedPost = postService.getPostById(postId);
        updatedPost.setText(postText);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Blob image = BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize());
                updatedPost.setImageFile(image);
            } catch (Exception e) {
                return "error_template";
            }
        }
        
        threadService.modifyPostFromThread(updatedPost);
        return "redirect:/t/" + threadName;
    }

}
