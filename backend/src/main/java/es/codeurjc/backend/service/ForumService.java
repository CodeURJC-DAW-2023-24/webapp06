package es.codeurjc.backend.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.codeurjc.backend.exceptions.ForumNotFoundException;
import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.repository.ForumRepository;
import es.codeurjc.backend.repository.ThreadRepository;

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;
    @Autowired
    private ThreadRepository threadRepository;

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

    public List<Forum> getTrendingForums(boolean logged, Long userid) {
        List<Forum> forums = new ArrayList<>();
        List<Long> forumsid = new ArrayList<>();
        if (logged){
            forumsid = forumRepository.findTop5CategoriesLastMonth(userid.toString());
        } else {
            forumsid = forumRepository.findForumsWithMostPostsLast48Hours();
        }
        if(!forumsid.isEmpty()){
            forums = forumRepository.findAllById(forumsid);
        }
        return forums;
    }

    public void addThreadToForum(Forum forum, Thread thread) {
        threadRepository.save(thread);
        forum.getThreads().add(thread);
        forumRepository.save(forum);
    }

    public Forum getForumById(Long id) throws ForumNotFoundException {
    return forumRepository.findById(id)
            .orElseThrow(() -> new ForumNotFoundException("Forum with id " + id + " not found"));
}

    public List<Forum> getAllForumsExcp() throws JsonProcessingException {
        return forumRepository.findAll();
    }

    public List<Forum> getTrendingForumsNoLog() {
        List<Forum> forums = new ArrayList<>();
        List<Long> forumsid = new ArrayList<>();
        forumsid = forumRepository.findForumsWithMostPostsLast48Hours();
        if (!forumsid.isEmpty()) {
            forums = forumRepository.findAllById(forumsid);
        }
        return forums;
    }

    public List<Forum> getTrendingForumsLog(Long userid) {
        List<Forum> forums = new ArrayList<>();
        List<Long> forumsid = new ArrayList<>();
        forumsid = forumRepository.findTop5CategoriesLastMonth(userid.toString());
        if (!forumsid.isEmpty()) {
            forums = forumRepository.findAllById(forumsid);
        }
        return forums;
    }
}
