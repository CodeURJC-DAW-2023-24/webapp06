package es.codeurjc.backend.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;


@Controller
public class LoginController {

  @Autowired
  private UserService userService;

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

  @GetMapping("isActive")
  public String isActive(Principal principal, HttpSession session) {
      User user = userService.getUserByUsername(principal.getName());
      if (user.getIsActive()) {
          return "redirect:/";
      }
      session.invalidate();
      return "inactive_template";
  }
  
  
}
