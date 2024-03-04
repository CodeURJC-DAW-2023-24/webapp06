package es.codeurjc.backend.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.ForumService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.UserService;

@Controller
@RequestMapping("/f")
public class ForumController {

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
    }

    @GetMapping("/{forumName}")
    public String getForum(Model model, Principal principal, @PathVariable String forumName) {
        Forum forum = forumService.getForumByName(forumName);
        List<Thread> threads = threadService.getThreadsByForum(forum);
        model.addAttribute("forum", forum);
        model.addAttribute("threads", threads);
        for (Thread thread : threads) {
            thread.setNumberPosts();
        }
        model.addAttribute("forums", forumService.getAllForums());
        return "forum_template";
    }

    @PostMapping("/{forumName}/addThread")
    public String addPost(Model model, Principal principal, @RequestParam("post-form-text") String threadName,
            @PathVariable String forumName) throws Exception {
        String name = principal.getName();
        User activeUser = userService.getUserByUsername(name);
        Forum forum = forumService.getForumByName(forumName);
        Thread thread = new Thread(threadName, forum, new ArrayList<Post>(), activeUser);
        forumService.addThreadToForum(forum, thread);
        return "redirect:/t/" + threadName;
    }
}
