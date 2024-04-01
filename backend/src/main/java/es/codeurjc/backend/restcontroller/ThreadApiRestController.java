package es.codeurjc.backend.restcontroller;

import java.net.URI;
import java.security.Principal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.service.ForumService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.dto.ThreadDTO;
import es.codeurjc.backend.dto.PostDTO;
import es.codeurjc.backend.dto.PostTimeDTO;
import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.service.ForumService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("api/threads")
public class ThreadApiRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ForumService forumService;
    @Autowired
    private PostService postService;

    final private String ERROR_OCURRED = "Error occurred, try again later";
    final private String AUTHENTICATED_USER_NOT_FOUND = "Authenticated user not found.";

    @GetMapping("/{forumName}")
    public ResponseEntity<?> getThreadsByForum(@PathVariable String forumName) {
        try {
            Forum forum = forumService.getForumByName(forumName);
            Page<Thread> threads = threadService.getPaginatedThreads(0,10,forum);
            Page<ThreadDTO> threadData = threads.map(thread -> new ThreadDTO(thread));
            return new ResponseEntity<>(threadData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Forum with name " + forumName + " not found.", HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/")
    @Operation(summary = "Create thread", description = "Create a new thread and add it to the forum.", responses = {
            @ApiResponse(responseCode = "201", description = "Thread created successfully", headers = @Header(name = "Location", description = "Link to the newly created thread")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> addThread(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ThreadDTO thread)
            throws Exception {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AUTHENTICATED_USER_NOT_FOUND);
        }
        //String name = userDetails.getUsername();
        //User activeUser = userService.getUserByUsername(name);
        Forum forum = thread.getForum();
        Thread newThread = new Thread(thread.getName(), thread.getForum(), thread.getPosts(), thread.getOwner());
        forumService.addThreadToForum(forum, newThread);

        String postUrl = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newThread.getId())
                .toUriString();

        return ResponseEntity.created(URI.create(postUrl)).body(newThread);
    }

/*
    @PutMapping("/{postId}")
    public ResponseEntity<?> editPost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String postId,
            PostDTO updatedPost) {
        if (userDetails != null && userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
            if (userService.isAdmin(userDetails.getUsername())) {
                try {
                    Post existingPost = postService.getPostById(Long.parseLong(postId));
                    updatedPost.setId(existingPost.getId());
                    try {
                        Post post = new Post(postId, null,
                                userService.getUserByUsername(updatedPost.getOwnerUsername()),
                                threadService.getThreadByName(updatedPost.getThreadName()), new ArrayList<>(),
                                new ArrayList<>(), updatedPost.getReports());
                        if (updatedPost.getThreadName() == existingPost.getThread().getName()) {
                            postService.savePost(post);
                        } else {
                            threadService.deletePostFromThread(existingPost.getThread(), existingPost.getId());
                            threadService.addPostToThread(threadService.getThreadByName(updatedPost.getThreadName()),
                                    post);
                        }
                        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
                    } catch (Exception e) {
                        return new ResponseEntity<>("Unable to edit post", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>("Post with id " + postId + " not found.", HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>(AUTHENTICATED_USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }*/

    @DeleteMapping("/{threadId}")
    public ResponseEntity<?> deleteThread(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String threadId) {
        if (userDetails != null && userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
            if (userService.isAdmin(userDetails.getUsername())) {
                try {
                    Thread thread = threadService.getThreadById(Long.parseLong(threadId));
                    threadService.deleteThread(thread);
                    return new ResponseEntity<>("Thread deleted", HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("Thread with Id " + threadId + " not found.", HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>(AUTHENTICATED_USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }

}
