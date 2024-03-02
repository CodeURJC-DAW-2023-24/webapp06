package es.codeurjc.backend.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String login(Model model, HttpServletRequest request) {
    CsrfToken token = (CsrfToken) request.getAttribute("_csrf"); 
    model.addAttribute("token", token.getToken());

    return "login_template";
  }

  @GetMapping("/loginerror")
  public String loginerror() {
    return "login_error_template";
  }
  
}
