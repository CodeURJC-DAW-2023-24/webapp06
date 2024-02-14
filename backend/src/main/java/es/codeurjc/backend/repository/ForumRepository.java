package es.codeurjc.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.backend.model.Forum;


public interface ForumRepository extends JpaRepository<Forum, Long> {
    Optional<Forum> findByName(String name);
}
