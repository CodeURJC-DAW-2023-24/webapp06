package es.codeurjc.backend.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.exceptions.ThreadNotFoundException;
import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.repository.ForumRepository;
import es.codeurjc.backend.repository.PostRepository;
import es.codeurjc.backend.repository.ThreadRepository;

@Service
public class ThreadService {

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ForumRepository forumRepository;

    public Thread getThreadById(Long id) {
        Thread thread = threadRepository.getReferenceById(id);
        if (thread.getName() == null) {
            throw new ThreadNotFoundException("Thread not found with id: " + id + ".");
        }
        return thread;
    }

    public Thread getThreadByName(String name) {
        return threadRepository.findByName(name)
                .orElseThrow(() -> new ThreadNotFoundException("Thread not found with name: " + name + "."));
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
            for (Post post : thread.getPosts()) {
                postRepository.delete(post);
            }
            Forum forum = forumRepository.findById(thread.getForum().getId()).orElse(null);
            if (forum != null) {
                forum.getThreads().remove(thread);
                forumRepository.save(forum);
            }
            threadRepository.delete(thread);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Page<Thread> getPaginatedThreads(int page, int size, Forum forum) {
        Pageable pageable = PageRequest.of(page, size);
        return threadRepository.findByForumPaginated(forum.getId(), pageable);
    }

    public void addPostToThread(Thread thread, Post post) {
        thread.getPosts().add(post);
        threadRepository.save(thread);
    }

    public void modifyPostFromThread(Post post) {
        postRepository.save(post);
    }

    public void deletePostFromThread(Thread thread, Long postId) {
        Post post = postRepository.getReferenceById(postId);
        thread.getPosts().remove(post);
        threadRepository.save(thread);
    }
}
