package es.codeurjc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
   
}