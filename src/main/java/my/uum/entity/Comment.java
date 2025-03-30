package my.uum.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a comment in the system.
 * Each instance of this class corresponds to a comment made by a user.
 *
 * @author group Capybara
 * @version 1.0
 * @since 2024-01-21
 */
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commenter")
    @Lob
    private String commenter;

    @Column(name = "comment_body", columnDefinition = "TEXT")
    @Lob
    private String commentBody;

    @Column(name = "word_count")
    @Lob
    private Integer wordCount;

    /**
     * Constructs a new Comment instance with the specified parameters.
     *
     * @param id          The unique identifier for the comment.
     * @param commenter   The username or identifier of the user who made the comment.
     * @param commentBody The textual content of the comment.
     * @param wordCount   The word count of the comment.
     */
    public Comment(Long id, String commenter, String commentBody, Integer wordCount) {
        this.id = id;
        this.commenter = commenter;
        this.commentBody = commentBody;
        this.wordCount = wordCount;
    }

    /**
     * Retrieves the unique identifier of the comment.
     *
     * @return The unique identifier of the comment.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the comment.
     *
     * @param id The unique identifier to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the username or identifier of the user who made the comment.
     *
     * @return The username or identifier of the commenter.
     */
    public String getCommenter() {
        return commenter;
    }

    /**
     * Sets the username or identifier of the user who made the comment.
     *
     * @param commenter The username or identifier to set.
     */
    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    /**
     * Retrieves the textual content of the comment.
     *
     * @return The textual content of the comment.
     */
    public String getCommentBody() {
        return commentBody;
    }

    /**
     * Sets the textual content of the comment.
     *
     * @param commentBody The textual content to set.
     */
    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    /**
     * Retrieves the word count of the comment.
     *
     * @return The word count of the comment.
     */
    public Integer getWordCount() {
        return wordCount;
    }

    /**
     * Sets the word count of the comment.
     *
     * @param wordCount The word count to set.
     */
    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }
}
