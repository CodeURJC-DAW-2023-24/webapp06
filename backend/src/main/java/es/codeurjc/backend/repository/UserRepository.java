package es.codeurjc.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.codeurjc.backend.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsername(String username);
   void deleteByUsername(String username);
   @Query("SELECT u FROM User u WHERE u.username LIKE :prefix%")
   List<User> findByUsernameStartingWith(@Param("prefix") String prefix);
}