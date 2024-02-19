package es.codeurjc.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.repository.ThreadRepository;

@Service
public class ThreadService {
    
    @Autowired
    private ThreadRepository threadRepository;

    public List<Thread> getThreadsByOwner(User owner){
        return threadRepository.findByOwner(owner).orElseThrow();
    }
}
