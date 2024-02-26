package es.codeurjc.backend.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.codeurjc.backend.service.ForumService;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ForumController {
    @Autowired
    private ForumService forumService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @RequestMapping("/{forum}")
    public String getForum(Model model, Principal principal, @PathVariable String forum) {
        return new String();
    }

    @GetMapping("/{forum}/{thread}")
    public String getThread(Model model, Principal principal, @PathVariable String forum, @PathVariable String thread) {
        return "thread";
    }

}
