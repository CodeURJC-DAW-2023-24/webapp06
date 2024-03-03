package es.codeurjc.backend.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.service.ForumService;
import es.codeurjc.backend.service.ThreadService;

@RestController
@RequestMapping("/thread")
public class ThreadRestController {

    @Autowired
    private ThreadService threadService;
    @Autowired
    private ForumService forumService;

    @GetMapping("/user/paginated")
    public Page<Thread> getPaginatedThreadsByUsername(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "username", required = true) String username) {
        return threadService.getPaginatedThreadsByUsername(page, size, username);
    }

    @GetMapping("/paginated")
    public Page<Thread> getPaginatedThreads(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(name = "nameForum", required = true) String nameForum) {
        Forum forum = forumService.getForumByName(nameForum);
        return threadService.getPaginatedThreads(page, size, forum);
    }

}
