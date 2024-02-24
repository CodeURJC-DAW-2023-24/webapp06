package es.codeurjc.backend.repository;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
    Optional<List<Thread>> findByOwner(User owner);

    Optional<Thread> findByName(String name);

    @Query("SELECT COUNT(t) FROM Thread t WHERE t.owner = :owner AND DATE(t.createdAt) = :date")
    Long countByCreatedAt(User owner, LocalDate date);

    @Query("SELECT COUNT(t) FROM Thread t WHERE t.owner = :owner AND FUNCTION('MONTH', t.createdAt) = :month AND FUNCTION('YEAR', t.createdAt) = :year")
    Long countByMonthAndYear(@Param("owner") User owner, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(t) FROM Thread t WHERE t.owner = :owner AND FUNCTION('YEAR', t.createdAt) = :year")
    Long countByYear(@Param("owner") User owner, @Param("year") int year);
}