package es.codeurjc.backend.controller;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

    @GetMapping("/profile/{username}")
    public String userProfile(Model model, Principal principal, @PathVariable String username) {
        User user = userService.getUserByUsername(username);
        List<Thread> threads = threadService.getThreadsByOwner(user);
        List<Post> post = postService.getPostByOwner(user);
        boolean equalUserOrAdmin = false;

        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            equalUserOrAdmin = userService.getEqualUserOrAdmin(principal.getName(), username);
        }

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
    public String deleteProfile(Model model, Principal principal, @PathVariable String username, HttpSession session) {
        boolean equalUserOrAdmin = userService.getEqualUserOrAdmin(principal.getName(), username);
        if (equalUserOrAdmin) {
            userService.deleteUser(username);
        }
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/users")
    public String users() {
        return "users_template";
    }

    @GetMapping("/edit-profile")
    public String editProfile(Model model, Principal principal,
            @RequestParam(value = "username", required = false) String username,
            HttpServletRequest request) {
        if (username != null && username != "") {
            if (!userService.isAdmin(principal.getName())) {
                String referrer = request.getHeader("Referer");
                return "redirect:" + referrer;
            }
            userService.getUserByUsername(username); // check user exist
        }

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf"); 
        model.addAttribute("token", token.getToken());
        model.addAttribute("username", username != null ? username : principal.getName());
        return "edit_profile_template";
    }

    @PostMapping("/update-profile/{usernameEdit}")
    public String updateProfile(
            Principal principal,
            @PathVariable String usernameEdit,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            HttpServletRequest request) {
        if (userService.getEqualUserOrAdmin(principal.getName(), usernameEdit)) {
            User user = userService.getUserByUsername(usernameEdit);
            Boolean anyChange = false;
            if (username != null && !username.isEmpty()) {
                user.setUsername(username);
                anyChange = true;
            }
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    user.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
                    anyChange = true;
                } catch (Exception e) {
                    return "error";
                }
            }
            if (anyChange) {
                userService.update(user);
                return "redirect:/user/profile/" + user.getUsername();
            } else {
                String referrer = request.getHeader("Referer");
                return "redirect:" + referrer;
            }
        }
        return "redirect:/";
    }

    @GetMapping("/activation/{username}")
    public String getMethodName(@PathVariable String username) {
        User user = userService.getUserByUsername(username);

        if (user.getIsActive()) {
            return "already_active_template";
        } else {
            user.setIsActive(true);
            userService.update(user);
            return "activation_template";
        }
    }
}
