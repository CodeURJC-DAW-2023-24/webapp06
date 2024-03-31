package es.codeurjc.backend.restcontroller;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.backend.dto.PostAddDTO;
import es.codeurjc.backend.dto.PostDTO;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/posts")
public class PostApiRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private PostService postService;

    final private String AUTHENTICATED_USER_NOT_FOUND = "Authenticated user not found.";
    final private String ADMIN_AUTHORIZATION_REQUIRED = "Admin authorization is required.";
    final private String ERROR_OCURRED = "Error occurred, try again later.";

    @PostMapping("/")
    @Operation(summary = "Create post", description = "Create a new post and add it to the thread.", responses = {
            @ApiResponse(responseCode = "201", description = "Post created successfully", headers = @Header(name = "Location", description = "Link to the newly created post")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> addPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PostAddDTO post)
            throws Exception {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AUTHENTICATED_USER_NOT_FOUND);
        }
        String name = userDetails.getUsername();
        User activeUser = userService.getUserByUsername(name);
        List<User> userLikes = new ArrayList<>();
        List<User> userDislikes = new ArrayList<>();
        if (post.isLiked()) {
            userLikes.add(activeUser);
        } else {
            userDislikes.add(activeUser);
        }
        int reports = 0;
        if (post.isReported()) {
            reports++;
        }
        Thread thread = threadService.getThreadById(post.getThreadId());
        Post newPost = new Post(post.getText(), null, activeUser, thread, userLikes, userDislikes, reports);
        threadService.addPostToThread(thread, newPost);

        String postUrl = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPost.getId())
                .toUriString();

        return ResponseEntity.created(URI.create(postUrl)).body(newPost);
    }

    @GetMapping("/")
    @Operation(summary = "Get all posts", description = "Gets all posts.", responses = {
            @ApiResponse(responseCode = "200", description = "Post created successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "200", description = "Post created successfully", headers = @Header(name = "Location", description = "Link to the newly created post")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getPosts(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "thread", required = false) Long threadId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "reported", required = false) boolean reported) {
        try {
            Page<Post> posts;
            if (threadId == null) {
                if (reported) {
                    if (userDetails == null) {
                        return new ResponseEntity<>(AUTHENTICATED_USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
                    }
                    if (userService.isAdmin(userDetails.getUsername())) {
                        posts = postService.getReportedPosts(page, size);
                    } else {
                        return new ResponseEntity<>(ADMIN_AUTHORIZATION_REQUIRED, HttpStatus.UNAUTHORIZED);
                    }
                } else {
                    posts = postService.getPostsPaginated(page, size);
                }
            } else {
                // Check if thread has been created by trying to access its name
                // If this is not done it will just return an empty page
                threadService.getThreadById(threadId).getName();

                posts = postService.getPostsByThread(threadId, page, size);
            }

            Page<PostDTO> postsDTO = posts.map(post -> new PostDTO(post));
            return new ResponseEntity<>(postsDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Thread with id " + threadId + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostsById(@PathVariable String postId) {
        try {
            Post savedPost = postService.getPostById(Long.parseLong(postId));
            PostDTO post = new PostDTO(savedPost);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Post with id " + postId + " not found.", HttpStatus.NOT_FOUND);
        }
    }

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
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String postId) {
        if (userDetails != null && userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
            if (userService.isAdmin(userDetails.getUsername())) {
                try {
                    Post post = postService.getPostById(Long.parseLong(postId));
                    threadService.deletePostFromThread(post.getThread(), post.getId());
                    return new ResponseEntity<>("Post deleted", HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("Post with id " + postId + " not found.", HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>(AUTHENTICATED_USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }

    // TODO Finish below

    @PutMapping("/{postId}/like")
    public boolean likePost(Model model, Principal principal, @PathVariable String postId) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            if (postService.addPostLike(Long.parseLong(postId), user)) {
                postService.removePostDislike(Long.parseLong(postId), user);
            } else {
                postService.removePostLike(Long.parseLong(postId), user);
            }
            return true;
        }
        return false;
    }

    @PutMapping("/{postId}/dislike")
    public boolean dislikePost(Model model, Principal principal, @PathVariable String postId) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            if (postService.addPostDislike(Long.parseLong(postId), user)) {
                postService.removePostLike(Long.parseLong(postId), user);
            } else {
                postService.removePostDislike(Long.parseLong(postId), user);
            }
            return true;
        }
        return false;
    }

    @PutMapping("/{postId}/report")
    public boolean getMethodName(Model model, Principal principal, @PathVariable String postId) {
        postService.reportPost(Long.parseLong(postId));
        return true;
    }

    @PutMapping("/{postId}/validate")
    public void validatePost(Model model, Principal principal, @PathVariable String postId) {
        postService.validatePost(Long.parseLong(postId));
    }

    @PutMapping("/{postId}/invalidate")
    public void invalidatePost(Model model, Principal principal, @PathVariable String postId) {
        postService.invalidatePost(Long.parseLong(postId));
    }
}
