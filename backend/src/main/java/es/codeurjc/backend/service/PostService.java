package es.codeurjc.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.codeurjc.backend.dto.PostAddDTO;
import es.codeurjc.backend.exceptions.PostNotFoundException;
import es.codeurjc.backend.exceptions.ThreadNotFoundException;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.PostRepository;
import es.codeurjc.backend.repository.ThreadRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ThreadRepository threadRepository;

    public List<Post> getPostByOwner(User owner) {
        return postRepository.findByOwner(owner).orElseThrow();
    }

    public List<Post> getReportedPosts() {
        return postRepository.findReportedPosts();
    }

    public Post getPostById(Long postId) {
        Post post = new Post();
        try {
            post = postRepository.getReferenceById(postId);
        } catch (Exception ex) {
            throw new PostNotFoundException("Error retrieving post with id: " + postId + ".");
        }
        return post;
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public Post updatePost(Post existingPost, PostAddDTO newPostData, User activeUser) throws BadRequestException {
        Post updatedPost = new Post(existingPost.getText(), existingPost.getImageFile(), existingPost.getOwner(),
                existingPost.getThread(), existingPost.getUserLikes(), existingPost.getUserDislikes(),
                existingPost.getReports());
        updatedPost.setId(existingPost.getId());

        if (newPostData.getText() != null) {
            if (newPostData.getText().isBlank()) {
                throw new BadRequestException("The text field can't be blank.");
            }
            updatedPost.setText(newPostData.getText());
        }

        if (newPostData.isLiked()) {
            if (!existingPost.getUserLikes().contains(activeUser)) {
                updatedPost.addUserLike(activeUser);
                if (existingPost.getUserDislikes().contains(activeUser)) {
                    updatedPost.removeUserDislike(activeUser);
                }
            }
        } else {
            if (existingPost.getUserLikes().contains(activeUser)) {
                updatedPost.removeUserLike(activeUser);
            }
        }

        if (newPostData.isDisliked()) {
            if (!existingPost.getUserDislikes().contains(activeUser)) {
                updatedPost.addUserDislike(activeUser);
                if (existingPost.getUserLikes().contains(activeUser)) {
                    updatedPost.removeUserLike(activeUser);
                }
            }
        } else {
            if (existingPost.getUserDislikes().contains(activeUser)) {
                updatedPost.removeUserDislike(activeUser);
            }
        }

        if (newPostData.isReported()) {
            updatedPost.setReports(existingPost.getReports() + 1);
        }

        if (newPostData.getThreadId() != null) {
            if (!threadRepository.existsById(newPostData.getThreadId())) {
                throw new ThreadNotFoundException("Thread not found with id: " + newPostData.getThreadId() + ".");
            }
            updatedPost.setThread(threadRepository.getReferenceById(newPostData.getThreadId()));
        }

        return updatedPost;
    }

    public boolean addPostLike(Long postId, User user) {
        if (!postRepository.getReferenceById(postId).getUserLikes().contains(user)) {
            Post post = postRepository.getReferenceById(postId);
            post.addUserLike(user);
            postRepository.save(post);
            return true;
        }
        return false;
    }

    public boolean removePostLike(Long postId, User user) {
        if (postRepository.getReferenceById(postId).getUserLikes().contains(user)) {
            Post post = postRepository.getReferenceById(postId);
            post.removeUserLike(user);
            postRepository.save(post);
            return true;
        }
        return false;
    }

    public boolean addPostDislike(Long postId, User user) {
        if (!postRepository.getReferenceById(postId).getUserDislikes().contains(user)) {
            Post post = postRepository.getReferenceById(postId);
            post.addUserDislike(user);
            postRepository.save(post);
            return true;
        }
        return false;
    }

    public boolean removePostDislike(Long postId, User user) {
        if (postRepository.getReferenceById(postId).getUserDislikes().contains(user)) {
            Post post = postRepository.getReferenceById(postId);
            post.removeUserDislike(user);
            postRepository.save(post);
            return true;
        }
        return false;
    }

    public void reportPost(Long postId) {
        Post post = postRepository.getReferenceById(postId);
        int reports = post.getReports();
        if (reports >= 0)
            post.setReports(post.getReports() + 1);
        postRepository.save(post);
    }

    public Post validatePost(Long postId) {
        Post post = postRepository.getReferenceById(postId);
        post.setReports(-1);
        postRepository.save(post);
        return post;
    }

    public Post invalidatePost(Long postId) {
        Post post = postRepository.getReferenceById(postId);
        if (post.getReports() < 0) {
            post.setReports(0);
            postRepository.save(post);
        }
        return post;
    }

    public Long getTotalPostsForDay(User owner, LocalDate date) {
        return postRepository.countByCreatedAt(owner, date);
    }

    public Long getTotalPostsForMonth(User owner, int month, int year) {
        return postRepository.countByMonthAndYear(owner, month, year);
    }

    public Long getTotalPostsForYear(User owner, int year) {
        return postRepository.countByYear(owner, year);
    }

    public Page<Post> getPostsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAll(pageable);
    }

    public Page<Post> getReportedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findPostsWithReports(pageable);
    }

    public Page<Post> getPostsByThread(Long threadId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByThreadId(threadId, pageable).get();
    }
}
