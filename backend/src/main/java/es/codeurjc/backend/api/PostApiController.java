package es.codeurjc.backend.api;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.backend.dto.PostAddDTO;
import es.codeurjc.backend.dto.PostDTO;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.UserService;

@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private PostService postService;

    @PostMapping("/")
    public ResponseEntity<?> addPost(Model model, Principal principal, @RequestBody PostAddDTO post) throws Exception {
        String name = principal.getName();
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
        return new ResponseEntity<>(newPost, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getPosts(@RequestParam(value = "thread", required = false) Long threadId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        if (threadId == null) {
            ArrayList<PostDTO> posts = new ArrayList<>();
            for (Post post : postService.getPosts()) {
                posts.add(new PostDTO(post));
            }
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        try {
            // Check if thread has been created by trying to access its name
            // If this is not done it will just return an empty page
            threadService.getThreadById(threadId).getName();

            Page<Post> posts = postService.getPostsByThread(threadId, page, size);
            Page<PostDTO> postsDTO = posts.map(post -> new PostDTO(post));
            return new ResponseEntity<>(postsDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Thread with id " + threadId + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/reported")
    public ResponseEntity<?> getReportedPaginated(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Post> posts = postService.getReportedPosts(page, size);
        Page<PostDTO> postsDTO = posts.map(post -> new PostDTO(post));
        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
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
    public ResponseEntity<?> editPost(Model model, Principal principal, @PathVariable String postId, PostDTO updatedPost) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            if (userService.isAdmin(principal.getName())) {
                try {
                    Post existingPost = postService.getPostById(Long.parseLong(postId));
                    updatedPost.setId(existingPost.getId());
                    try {
                        Post post = new Post(postId, null, userService.getUserByUsername(updatedPost.getOwnerUsername()), threadService.getThreadByName(updatedPost.getThreadName()), new ArrayList<>(), new ArrayList<>(), updatedPost.getReports());
                        if (updatedPost.getThreadName() == existingPost.getThread().getName()) {
                            postService.savePost(post);
                        } else {
                            threadService.deletePostFromThread(existingPost.getThread(), existingPost.getId());
                            threadService.addPostToThread(threadService.getThreadByName(updatedPost.getThreadName()), post);
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
        return new ResponseEntity<>("Unable to edit post", HttpStatus.UNAUTHORIZED);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<?> patchPost(Model model, Principal principal, @PathVariable String postId,
            Post updatedPost) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            if (userService.isAdmin(principal.getName())) {
                //try {
                    Post existingPost = postService.getPostById(Long.parseLong(postId));

                    if (updatedPost.getText() != null) {
                        existingPost.setText(updatedPost.getText());
                    }
                    if (updatedPost.getImageFile() != null) {
                        existingPost.setImageFile(updatedPost.getImageFile());
                    }
                    if (updatedPost.getOwner() != null) {
                        existingPost.setOwner(updatedPost.getOwner());
                    }
                    if (updatedPost.getCreatedAt() != null) {
                        existingPost.setCreatedAt(updatedPost.getCreatedAt());
                    }
                    if (updatedPost.getUserLikes() != null) {
                        existingPost.setUserLikes(updatedPost.getUserLikes());
                    }
                    if (updatedPost.getUserDislikes() != null) {
                        existingPost.setUserDislikes(updatedPost.getUserDislikes());
                    }
                    if (updatedPost.getReports() != null) {
                        existingPost.setReports(updatedPost.getReports());
                    }
                    if (updatedPost.getThread() != null) {
                        if (updatedPost.getThread() == existingPost.getThread()) {
                            postService.savePost(existingPost);
                        } else {
                            threadService.deletePostFromThread(existingPost.getThread(), existingPost.getId());
                            threadService.addPostToThread(updatedPost.getThread(), existingPost);
                        }
                    } else {
                        postService.savePost(existingPost);
                    }
                    return new ResponseEntity<>(new PostDTO(existingPost), HttpStatus.OK);
                //} catch (Exception e) {
                    //return new ResponseEntity<>("Post with id " + postId + " not found.", HttpStatus.NOT_FOUND);
                //}
            }
        }
        return new ResponseEntity<>("Unable to patch post", HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(Model model, Principal principal, @PathVariable String postId) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            if (userService.isAdmin(principal.getName())) {
                try {
                    Post post = postService.getPostById(Long.parseLong(postId));
                    threadService.deletePostFromThread(post.getThread(), post.getId());
                    return new ResponseEntity<>("Post deleted", HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("Post with id " + postId + " not found.", HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>("Unable to delete post", HttpStatus.UNAUTHORIZED);
    }
}
