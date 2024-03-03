package es.codeurjc.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.repository.ForumRepository;

@Service
public class ForumService {
    @Autowired
    private ForumRepository forumRepository;

    public List<Forum> getAllForums() {
        return forumRepository.findAll();
    }

    public Forum getForumByName(String name) {
        return forumRepository.findByName(name).orElseThrow();
    }

    public boolean removeThreadFromForum(Forum forum, Thread thread) {
        forum.getThreads().remove(thread);
        return true;
    }
}
