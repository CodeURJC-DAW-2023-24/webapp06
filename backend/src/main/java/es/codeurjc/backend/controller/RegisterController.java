package es.codeurjc.backend.controller;

import java.security.Principal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/register")
public class RegisterController {

  @Autowired
  private UserService userService;

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

  @GetMapping
  public String register(Model model, HttpServletRequest request) {
    CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
    model.addAttribute("token", token.getToken());
    return "register_template";
  }

  @PostMapping("/post")
  public String register(@RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password,
      @RequestParam(value = "email") String email) {
    Optional<User> user = userService.getUserByUsernameNoException(username);
    Optional<User> user2 = userService.getUserByEmail(email);
    if (user.isPresent() || user2.isPresent()) {
      return "register_error_template";
    } else {
      userService.createUser(username, email, password);
      return "register_done_template";
    }
  }
}
