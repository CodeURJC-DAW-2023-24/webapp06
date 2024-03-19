package es.codeurjc.backend.api;

import java.net.http.HttpResponse;
import java.security.Principal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.HttpResource;

import es.codeurjc.backend.dto.PostAddDTO;
import es.codeurjc.backend.dto.PostDTO;
import es.codeurjc.backend.dto.PostReportDTO;
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
    public ResponseEntity<?> getPosts(@RequestParam(required = false) Long threadId,
            @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        if (threadId == null) {
            ArrayList<PostDTO> posts = new ArrayList<>();
            for (Post post : postService.getPosts()) {
                posts.add(new PostDTO(post));
            }
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        if (page == null || size == null) {
            ArrayList<PostDTO> posts = new ArrayList<>();
            for (Post post : postService.getPostsByThread(threadId, 1, 100)) {
                posts.add(new PostDTO(post));
            }
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        return new ResponseEntity<>(postService.getPostsByThread(threadId, page, size), HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostsById(@PathVariable String postId) {
        Post savedPost = postService.getPostById(Long.parseLong(postId));
        if (savedPost == null) {
            return new ResponseEntity<>("Post with id " + postId + " not found.", HttpStatus.NOT_FOUND);
        }
        PostDTO post = new PostDTO(savedPost);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/reported")
    public ResponseEntity<Page<PostReportDTO>> getReportedPaginated(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Post> posts = postService.getReportedPosts(page, size);
        Page<PostReportDTO> postsDTO = posts.map(post -> new PostReportDTO(post));
        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(Model model, Principal principal, @PathVariable String postId, Post post) {
        Post existingPost = postService.getPostById(Long.parseLong(postId));
        if (existingPost != null) {
            post.setId(post.getId());
            postService.getPostById(post.getId());
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(Model model, Principal principal, @PathVariable String postId) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            String username = principal.getName();
            Post post = postService.getPostById(Long.parseLong(postId));
            Thread thread = post.getThread();
            if (post.getOwner().getUsername() == username || userService.isAdmin(username)) {
                threadService.deletePostFromThread(thread, Long.parseLong(postId));
                return new ResponseEntity<>("Post deleted", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Unable to delete post", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<String> likePost(Model model, Principal principal, @PathVariable String postId) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            if (postService.addPostLike(Long.parseLong(postId), user)) {
                postService.removePostDislike(Long.parseLong(postId), user);
            } else {
                postService.removePostLike(Long.parseLong(postId), user);
            }
            return new ResponseEntity<>("Post liked", HttpStatus.OK);
        }
        return new ResponseEntity<>("Unable to like post", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/{postId}/dislike")
    public ResponseEntity<String> dislikePost(Model model, Principal principal, @PathVariable String postId) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            if (postService.addPostDislike(Long.parseLong(postId), user)) {
                postService.removePostLike(Long.parseLong(postId), user);
            } else {
                postService.removePostDislike(Long.parseLong(postId), user);
            }
            return new ResponseEntity<>("Post disliked", HttpStatus.OK);
        }
        return new ResponseEntity<>("Unable to dislike post", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/{postId}/report")
    public boolean reprtPost(Model model, Principal principal, @PathVariable String postId) {
        postService.reportPost(Long.parseLong(postId));
        return true;
    }

    @GetMapping("/{postId}/validate")
    public void validatePost(Model model, Principal principal, @PathVariable String postId) {
        postService.validatePost(Long.parseLong(postId));
    }

    @GetMapping("/{postId}/invalidate")
    public void invalidatePost(Model model, Principal principal, @PathVariable String postId) {
        postService.invalidatePost(Long.parseLong(postId));
    }
}
