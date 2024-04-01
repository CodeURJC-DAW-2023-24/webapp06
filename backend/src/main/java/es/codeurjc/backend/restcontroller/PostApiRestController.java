package es.codeurjc.backend.restcontroller;

import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import es.codeurjc.backend.exceptions.ThreadNotFoundException;
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
import jakarta.persistence.EntityNotFoundException;

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
    final private String OWNER_ADMIN_AUTHORIZATION_REQUIRED = "Owner or Admin authorization is required.";
    final private String ADMIN_AUTHORIZATION_REQUIRED = "Admin authorization is required.";
    final private String ERROR_OCURRED = "Server error occurred, try again later.";

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

        if (post.getText() == null || post.getText().isBlank()) {
            return new ResponseEntity<>("The text field can't be null or blank.", HttpStatus.BAD_REQUEST);
        }

        Blob image = null;
        if (post.getImageFile() != null) {
            byte[] imageBytes = null;
            try {
                imageBytes = Base64.getDecoder().decode(post.getImageFile());
            } catch (Exception e) {
                return new ResponseEntity<>("Image data is not valid Base64 encoded data.", HttpStatus.BAD_REQUEST);
            }
            try {
                image = new SerialBlob(imageBytes);
            } catch (SQLException e) {
                return new ResponseEntity<>(ERROR_OCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        List<User> userLikes = new ArrayList<>();
        List<User> userDislikes = new ArrayList<>();
        if (post.isLiked()) {
            userLikes.add(activeUser);
        }
        if (post.isDisliked()) {
            userDislikes.add(activeUser);
        }

        int reports = 0;
        if (post.isReported()) {
            reports++;
        }

        Thread thread;
        try {
            thread = threadService.getThreadById(post.getThreadId());
        } catch (Exception e) {
            return new ResponseEntity<>("Thread not found with id: " + post.getThreadId() + ".", HttpStatus.NOT_FOUND);
        }

        Post newPost = new Post(post.getText(), image, activeUser, thread, userLikes, userDislikes, reports);
        threadService.addPostToThread(thread, newPost);

        String postUrl = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPost.getId())
                .toUriString();

        return ResponseEntity.created(URI.create(postUrl)).body(new PostDTO(newPost));
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
                try {
                    threadService.getThreadById(threadId);
                    posts = postService.getPostsByThread(threadId, page, size);
                } catch (Exception e) {
                    return new ResponseEntity<>("Thread not found with id: " + threadId + ".", HttpStatus.NOT_FOUND);
                }

            }

            Page<PostDTO> postsDTO = posts.map(post -> new PostDTO(post));
            return new ResponseEntity<>(postsDTO, HttpStatus.OK);
        } catch (ThreadNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostsById(@PathVariable String postId) {
        try {
            Post savedPost = postService.getPostById(Long.parseLong(postId));
            PostDTO post = new PostDTO(savedPost);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> editPost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String postId,
            @RequestBody PostAddDTO updatedPostInfo,
            @RequestParam(value = "validate", required = false) boolean validate) {
        if (userDetails != null) {
            try {
                Post existingPost = postService.getPostById(Long.parseLong(postId));
                if (existingPost.getOwner().getUsername().equals(userDetails.getUsername())
                        || userService.isAdmin(userDetails.getUsername())) {
                    User activeUser = userService.getUserByUsername(userDetails.getUsername());

                    Blob image = null;
                    if (updatedPostInfo.getImageFile() != null) {
                        byte[] imageBytes = null;
                        try {
                            imageBytes = Base64.getDecoder().decode(updatedPostInfo.getImageFile());
                        } catch (Exception e) {
                            return new ResponseEntity<>("Image data is not valid Base64 encoded data.",
                                    HttpStatus.BAD_REQUEST);
                        }
                        try {
                            image = new SerialBlob(imageBytes);
                        } catch (SQLException e) {
                            return new ResponseEntity<>(ERROR_OCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }

                    Post newPost = postService.updatePost(existingPost, updatedPostInfo, activeUser, image);
                    if (newPost.getThread().getId() != existingPost.getThread().getId()) {
                        threadService.deletePostFromThread(existingPost.getThread(), existingPost.getId());
                        threadService.addPostToThread(newPost.getThread(), newPost);
                    } else {
                        postService.savePost(newPost);
                    }

                    newPost = postService.invalidatePost(Long.parseLong(postId));

                    if (validate) {
                        if (userService.isAdmin(userDetails.getUsername())) {
                            newPost = postService.validatePost(Long.parseLong(postId));
                        } else {
                            return new ResponseEntity<>(ADMIN_AUTHORIZATION_REQUIRED, HttpStatus.UNAUTHORIZED);
                        }
                    }

                    return new ResponseEntity<>(new PostDTO(newPost), HttpStatus.OK);
                }
                return new ResponseEntity<>(OWNER_ADMIN_AUTHORIZATION_REQUIRED, HttpStatus.UNAUTHORIZED);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } catch (BadRequestException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (ThreadNotFoundException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(AUTHENTICATED_USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String postId) {
        if (userDetails != null) {
            if (userService.isAdmin(userDetails.getUsername())) {
                try {
                    Post post = postService.getPostById(Long.parseLong(postId));
                    PostDTO postDTO = new PostDTO(post);
                    threadService.deletePostFromThread(post.getThread(), post.getId());
                    return new ResponseEntity<>(postDTO, HttpStatus.OK);
                } catch (NumberFormatException e) {
                    return new ResponseEntity<>("Invalid postId format. Please provide a valid numeric id.",
                            HttpStatus.BAD_REQUEST);
                } catch (EntityNotFoundException e) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>(AUTHENTICATED_USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }
}
