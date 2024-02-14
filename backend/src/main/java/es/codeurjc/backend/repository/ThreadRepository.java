package es.codeurjc.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;


public interface ThreadRepository extends JpaRepository<Thread, Long> {
    //Optional<Thread> findByUser(User user);

    Optional<Thread> findByName(String name);
}