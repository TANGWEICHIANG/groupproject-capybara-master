package my.uum.jpa;

import my.uum.entity.Comment2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Comment2 entities.
 * Provides methods to save Comment2 entities and retrieve commenter counts.
 * <p>
 * This class is annotated with @Service to indicate that it is a Spring-managed service bean.
 * It interacts with the CommentRepository2 for database operations.
 *
 * @author group Capybara
 * @version 1.0
 * @since 2024-01-21
 */
@Service
public class CommentService2 {

    private final CommentRepository2 commentRepository2;

    /**
     * Constructor for CommentService2.
     *
     * @param commentRepository2 The CommentRepository2 to be used for database operations.
     */
    @Autowired
    public CommentService2(CommentRepository2 commentRepository2) {
        this.commentRepository2 = commentRepository2;
    }

    /**
     * Saves a Comment2 entity in the database.
     *
     * @param comment2 The Comment2 object to be saved.
     */
    @Transactional
    public void saveComment2(Comment2 comment2) {
        commentRepository2.save(comment2);
    }

    /**
     * Retrieves commenter counts for Comment2 entities from the database.
     *
     * @return A list of Object arrays where each array contains the commenter and the corresponding comment count.
     */
    @Transactional(readOnly = true)
    public List<Object[]> findCommenterCounts() {
        return commentRepository2.findCommenterCounts();
    }

}
