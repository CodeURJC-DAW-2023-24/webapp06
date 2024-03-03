package es.codeurjc.backend.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.repository.ForumRepository;
import es.codeurjc.backend.repository.PostRepository;
import es.codeurjc.backend.repository.ThreadRepository;

@Service
public class ThreadService {

    @Autowired
    private ForumRepository forumRepository;
    
    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private PostRepository postRepository;

    public Thread getThreadByName(String name) {
        return threadRepository.findByName(name).orElseThrow();
    }

    public List<Thread> getThreads() {
        return threadRepository.findAll();
    }

    public List<Thread> getThreadsByOwner(User owner) {
        return threadRepository.findByOwner(owner).orElseThrow();
    }

    public List<Thread> getThreadsByForum(Forum forum) {
        return threadRepository.findByForum(forum).orElseThrow();
    }

    public Long getTotalThreadsForDay(User owner, LocalDate date) {
        return threadRepository.countByCreatedAt(owner, date);
    }

    public Long getTotalThreadsForMonth(User owner, int month, int year) {
        return threadRepository.countByMonthAndYear(owner, month, year);
    }

    public Long getTotalThreadsForYear(User owner, int year) {
        return threadRepository.countByYear(owner, year);
    }

    public Page<Thread> getPaginatedThreadsByUsername(int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size);
        return threadRepository.findByUsernamePaginated(username, pageable);
    }
    
    public Boolean deleteThread(Thread thread) {
        try {
            for (Post post : thread.getPosts())
                postRepository.delete(post);
            threadRepository.delete(thread);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
