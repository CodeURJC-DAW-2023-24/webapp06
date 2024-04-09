package es.codeurjc.backend.restcontroller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
import io.swagger.v3.oas.annotations.media.Schema;
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
            @ApiResponse(responseCode = "201", description = "Post created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class)) }, headers = @Header(name = "Location", description = "Link to the newly created post")),
            @ApiResponse(responseCode = "400", description = "Bad request body", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Thread not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
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
            @ApiResponse(responseCode = "200", description = "Posts found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request body", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Thread not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
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
    @Operation(summary = "Get a post", description = "Gets a post.", responses = {
            @ApiResponse(responseCode = "200", description = "Post found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> getPostById(@PathVariable String postId) {
        try {
            Post savedPost = postService.getPostById(Long.parseLong(postId));
            PostDTO post = new PostDTO(savedPost);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{postId}/image")
    @Operation(summary = "Get a post image", description = "Gets a post image.", responses = {
            @ApiResponse(responseCode = "200", description = "Image found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Image not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> getPostImageById(@PathVariable Long postId) {
        try {
            Post post = postService.getPostById(postId);
            try {

                InputStream inputStream = post.getImageFile().getBinaryStream();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                byte[] imageBytes = outputStream.toByteArray();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);

                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } catch (NullPointerException e) {
                return new ResponseEntity<>("This post doesn't have an image.", HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{postId}")
    @Operation(summary = "Edit a post", description = "Edits a post.", responses = {
            @ApiResponse(responseCode = "200", description = "Post edited successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request body", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post or Thread not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> editPost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String postId,
            @RequestBody PostAddDTO updatedPostInfo,
            @RequestParam(value = "validate", required = false) boolean validate) {
        if (userDetails != null) {
            try {
                Post existingPost = postService.getPostById(Long.parseLong(postId));
                User activeUser = userService.getUserByUsername(userDetails.getUsername());

                Blob image = null;
                boolean changeThread = false;

                if (updatedPostInfo.getText() != null || updatedPostInfo.getImageFile() != null
                        || updatedPostInfo.getThreadId() != null) {
                    if (existingPost.getOwner().getUsername().equals(userDetails.getUsername())
                            || userService.isAdmin(userDetails.getUsername())) {

                        if (updatedPostInfo.getText().isBlank()) {
                            return new ResponseEntity<>("The text field can't be blank.", HttpStatus.BAD_REQUEST);
                        }

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

                        if (updatedPostInfo.getThreadId() != existingPost.getThread().getId()) {
                            changeThread = true;
                        }
                    } else {
                        return new ResponseEntity<>(OWNER_ADMIN_AUTHORIZATION_REQUIRED, HttpStatus.UNAUTHORIZED);
                    }
                }

                Post newPost = postService.updatePost(existingPost, updatedPostInfo, activeUser, image);

                if (changeThread) {
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

    @PutMapping("/{postId}/image")
    @Operation(summary = "Edit a post image", description = "Edits a post image.", responses = {
            @ApiResponse(responseCode = "200", description = "Post image edited successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request body", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post or Thread not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> editPostImage(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String postId, @RequestPart MultipartFile image) {
        if (userDetails != null) {
            try {
                Post post = postService.getPostById(Long.parseLong(postId));

                if (post.getOwner().getUsername().equals(userDetails.getUsername())
                        || userService.isAdmin(userDetails.getUsername())) {
                    if (image == null) {
                        return new ResponseEntity<>("Include an image.", HttpStatus.BAD_REQUEST);
                    }

                    try {
                        Blob imageFile = new SerialBlob(image.getBytes());
                        post.setImageFile(imageFile);
                        postService.savePost(post);

                        return new ResponseEntity<>("Image changed successfully.", HttpStatus.OK);
                    } catch (SQLException | IOException e) {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity<>(OWNER_ADMIN_AUTHORIZATION_REQUIRED, HttpStatus.UNAUTHORIZED);
                }
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(AUTHENTICATED_USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete a post", description = "Deletes a post.", responses = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PostDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request body", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String postId) {
        if (userDetails != null) {
            try {
                Post post = postService.getPostById(Long.parseLong(postId));
                if (post.getOwner().getUsername().equals(userDetails.getUsername())
                        || userService.isAdmin(userDetails.getUsername())) {
                    PostDTO postDTO = new PostDTO(post);
                    threadService.deletePostFromThread(post.getThread(), post.getId());
                    return new ResponseEntity<>(postDTO, HttpStatus.OK);
                }
                return new ResponseEntity<>(OWNER_ADMIN_AUTHORIZATION_REQUIRED, HttpStatus.UNAUTHORIZED);
            } catch (NumberFormatException e) {
                return new ResponseEntity<>("Invalid postId format. Please provide a valid numeric id.",
                        HttpStatus.BAD_REQUEST);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(AUTHENTICATED_USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }
}
