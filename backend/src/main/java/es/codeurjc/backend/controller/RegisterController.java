package es.codeurjc.backend.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

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

  @GetMapping("/register")
  public String register() {
    return "register";
  }

  @PostMapping("/registerUser")
  public String register(@RequestBody String username, @RequestBody String password, @RequestBody String email) {
    return "register_done";
  }

}
