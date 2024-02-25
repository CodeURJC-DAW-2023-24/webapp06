package es.codeurjc.backend.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.User;
import jakarta.transaction.Transactional;

public interface PostRepository extends JpaRepository<Post, Long> {
  Optional<List<Post>> findByOwner(User owner);

  Optional<Post> findByText(String text);

  @Transactional
  @Modifying
  @Query("UPDATE Post p SET p.owner.id = :newOwnerId WHERE p.owner.id = :currentOwnerId")
  void changeOwnerOfPosts(@Param("currentOwnerId") Long currentOwnerId, @Param("newOwnerId") Long newOwnerId);
}
