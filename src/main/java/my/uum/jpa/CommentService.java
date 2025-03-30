package my.uum.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.uum.entity.Comment;

import java.util.List;

/**
 * Service class for managing Comment entities.
 * Provides methods to save comments and retrieve commenter counts.
 * <p>
 * This class is annotated with @Service to indicate that it is a Spring-managed service bean.
 * It interacts with the CommentRepository for database operations.
 *
 * @author group Capybara
 * @version 1.0
 * @since 2024-01-21
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * Constructor for CommentService.
     *
     * @param commentRepository The CommentRepository to be used for database operations.
     */
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Saves a comment in the database.
     *
     * @param comment The Comment object to be saved.
     */
    @Transactional
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    /**
     * Retrieves commenter counts from the database.
     *
     * @return A list of Object arrays where each array contains the commenter and the corresponding comment count.
     */
    @Transactional(readOnly = true)
    public List<Object[]> findCommenterCounts() {
        return commentRepository.findCommenterCounts();
    }

}
