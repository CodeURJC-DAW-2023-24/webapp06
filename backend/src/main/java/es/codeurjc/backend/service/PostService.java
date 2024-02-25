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
