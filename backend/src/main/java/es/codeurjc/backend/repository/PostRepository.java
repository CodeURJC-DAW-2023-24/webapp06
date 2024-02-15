package es.codeurjc.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.User;


public interface PostRepository extends JpaRepository<Post, Long> {
  Optional<Post> findByOwner(User owner);
  Optional<Post> findByText(String text);
}
