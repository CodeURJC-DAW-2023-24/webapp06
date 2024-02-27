package es.codeurjc.backend.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.service.ForumService;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ForumController {
    @Autowired
    private ForumService forumService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private UserService userService;

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
	}

    @RequestMapping("/f/{forum}")
    public String getForum(Model model, Principal principal, @PathVariable String forum) {
        return "forum";
    }

    @GetMapping("/f/{forum}/t/{thread}")
    public String getThread(Model model, Principal principal, @PathVariable String forum, @PathVariable String thread) {
        Thread threadSelected = threadService.getThreadByName(thread);
        //List<Post> post = threadService.getPosts(thread);

        model.addAttribute("threadName", threadSelected.getName());

        return "thread";
    }

}
