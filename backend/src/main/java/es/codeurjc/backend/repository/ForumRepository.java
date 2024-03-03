package es.codeurjc.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.codeurjc.backend.model.Forum;

public interface ForumRepository extends JpaRepository<Forum, Long> {
    Optional<Forum> findByName(String name);
    
    @Query(value = "SELECT f.id " +
                "FROM forums f " + 
                "JOIN forums_threads ft ON f.id = ft.forum_id " + 
                "JOIN threads_posts tp ON ft.threads_id = tp.thread_id " + 
                "JOIN posts p ON tp.posts_id = p.id " + 
                "WHERE p.created_at >= DATE_SUB(NOW(), INTERVAL 48 HOUR) " +
                "GROUP BY f.id, f.name " + 
                "ORDER BY COUNT(p.id) DESC LIMIT 3", nativeQuery = true)
    List<Long> findForumsWithMostPostsLast48Hours();

    @Query(value = "SELECT f.id " +
            "FROM forums f " +
            "JOIN forums_threads ft ON f.id = ft.forum_id " +
            "JOIN threads_posts tp ON ft.threads_id = tp.thread_id " +
            "JOIN posts p ON tp.posts_id = p.id " +
            "WHERE p.created_at >= DATE_SUB(NOW(), INTERVAL 48 HOUR) " +
            "AND p.owner_id = :userid " +
            "GROUP BY f.id, f.name " +
            "ORDER BY COUNT(p.id) DESC LIMIT 3", nativeQuery = true)
    List<Long> findTop5CategoriesLastMonth(@Param("userid") String userId);





}
