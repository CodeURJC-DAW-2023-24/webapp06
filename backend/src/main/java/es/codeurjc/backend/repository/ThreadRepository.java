package es.codeurjc.backend.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;


public interface ThreadRepository extends JpaRepository<Thread, Long> {
    Optional<List<Thread>> findByOwner(User owner);

    Optional<Thread> findByName(String name);
}