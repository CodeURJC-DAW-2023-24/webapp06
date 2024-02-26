package es.codeurjc.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class RegisterController {

  @GetMapping("/register")
  public String register() {
    return "register";
  }
  
  @PostMapping("/registerUser")
  public String register(@RequestBody String username, @RequestBody String password, @RequestBody String email) {
      return "register_done";
  }
  
}
