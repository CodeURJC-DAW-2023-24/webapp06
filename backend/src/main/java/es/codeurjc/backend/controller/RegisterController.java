package es.codeurjc.backend.controller;

import java.security.Principal;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  public String register() {
    return "register";
  }

  @PostMapping("/post")
  public String register(@RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password,
      @RequestParam(value = "email") String email) {

    // User user = (User) userService.getUserByUsername(username);
    // User user2 = (User) userService.getUserByEmail(email);
    return "register_done";
  }

}
