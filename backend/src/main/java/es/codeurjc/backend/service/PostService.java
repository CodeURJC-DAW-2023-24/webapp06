package es.codeurjc.backend.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Post getPostById(Long postId){
        Post post = postRepository.getReferenceById(postId);
        return post;
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

    public void validatePost(Long postId) {
        Post post = postRepository.getReferenceById(postId);
        post.setReports(-1);
        postRepository.save(post);
    }
    
    public void invalidatePost(Long postId) {
        Post post = postRepository.getReferenceById(postId);
        if (post.getReports() < 0) {
            post.setReports(0);
            postRepository.save(post);
        }
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

    public Page<Post> getReportedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findPostsWithReports(pageable);
    }

    public Page<Post> getPostsByThread(Long threadId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByThreadId(threadId, pageable);
    }
}
