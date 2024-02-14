package es.codeurjc.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.User;


public interface PostRepository extends JpaRepository<Post, Long> {
  //Optional<Post> findByUser(User user);

  Optional<Post> findByText(String text);
}
