package es.codeurjc.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getPostByOwner(User owner) {
        return postRepository.findByOwner(owner).orElseThrow();
    }

    public List<Post> getReportedPosts() {
        return postRepository.findReportedPosts();
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

    public void validatePost(Long postId) {
        Post post = postRepository.getReferenceById(postId);
        post.setReports(-1);
        postRepository.save(post);
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
}
