package my.uum.jpa;

import my.uum.entity.Comment2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing Comment2 entities using Spring Data JPA.
 * Provides CRUD (Create, Read, Update, Delete) operations for Comment2 entities.
 * Additionally, includes a custom query to retrieve commenter counts.
 *
 * @author group Capybara
 * @version 1.0
 * @since 2024-01-21
 */
public interface CommentRepository2 extends JpaRepository<Comment2, Long> {

    /**
     * Custom query to retrieve commenter counts.
     *
     * @return A list of Object arrays where each array contains the commenter and the corresponding comment count.
     */
    @Query("SELECT c.commenter, COUNT(c) FROM Comment2 c GROUP BY c.commenter")
    List<Object[]> findCommenterCounts();
}
