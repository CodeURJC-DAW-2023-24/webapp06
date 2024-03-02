package es.codeurjc.backend.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.repository.ThreadRepository;

@Service
public class ThreadService {

    @Autowired
    private ThreadRepository threadRepository;

    public Thread getThreadByName(String name) {
        return threadRepository.findByName(name).orElseThrow();
    }

    public List<Thread> getThreads() {
        return threadRepository.findAll();
    }

    public List<Thread> getThreadsByOwner(User owner) {
        return threadRepository.findByOwner(owner).orElseThrow();
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

    public Object getThreadsByForum(Forum forum) {
        return threadRepository.findByForum(forum);
    }
}
