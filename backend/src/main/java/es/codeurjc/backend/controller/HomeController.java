package es.codeurjc.backend.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.ForumService;
import es.codeurjc.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    private ForumService forumService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

			model.addAttribute("logged", true);
			model.addAttribute("username", principal.getName());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
            User u = userService.getUserByUsername(principal.getName());
            model.addAttribute("trending",forumService.getTrendingForums(true,u.getId()));
		} else {
			model.addAttribute("logged", false);
            model.addAttribute("trending",forumService.getTrendingForums(false,null));
		}
        model.addAttribute("forums",forumService.getAllForums());
        
	}


    @GetMapping("/")
    public String login() {
        return "home";    
    }


}
