package es.codeurjc.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String login() {
    return "login_template";
  }

  @GetMapping("/loginerror")
  public String loginerror() {
    return "login_error_template";
  }
  
}
