package es.codeurjc.backend.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.codeurjc.backend.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsername(String username);
   Optional<User> findByEmail(String email);
   Optional<User> findById(Long id);
   void deleteByUsername(String username);
   @Query("SELECT u FROM User u WHERE u.username LIKE :prefix%")
    Page<User> findByUsernameStartingWith(@Param("prefix") String prefix, Pageable pageable);
}