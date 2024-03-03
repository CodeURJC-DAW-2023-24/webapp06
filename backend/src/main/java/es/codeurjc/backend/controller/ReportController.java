package es.codeurjc.backend.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.service.PostService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReportController {

	@Autowired
	PostService postService;

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

    @GetMapping("/reports")
    public String getReports(Model model) {
		model.addAttribute("posts", postService.getReportedPosts());
        return "reports";
    }
}
