package es.codeurjc.backend.service;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

    public List<Forum> getTrendingForums(boolean logged) {
        List<Forum> forums = new ArrayList<>();
        if (logged){
            
        } else {
            List<Long>forumsid = forumRepository.findForumsWithMostPostsLast48Hours();
            if(!forumsid.isEmpty()){
                forums = forumRepository.findAllById(forumsid);
            }
        }
        
        return forums;
    }

}
