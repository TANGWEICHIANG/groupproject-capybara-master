package my.uum.kafka;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import my.uum.entity.Comment;
import my.uum.jpa.CommentService;
import my.uum.telegram.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Kafka Consumer service for processing messages from "topicOne".
 * Handles the consumption of messages, processing and updating commenter and word counts,
 * and communicates with the TelegramBot to send the results to a chat.
 *
 * This class is annotated with @Service to indicate that it is a Spring-managed service bean.
 * Utilizes the CommentService for database operations and communicates with the TelegramBot.
 *
 * @author group Capybara
 * @version 1.0
 * @since 2024-01-21
 */
@Service
public class KafkaConsumer1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer1.class);
    private final Map<String, Integer> commenterCount = new HashMap<>();
    private final Map<String, Integer> wordCount = new HashMap<>();

    @Autowired
    private CommentService commentService;

    private TelegramBot telegramBot;
    private List<Object[]> commenterCounts;

    /**
     * Sets the TelegramBot instance for communication.
     *
     * @param telegramBot The TelegramBot instance.
     */
    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Retrieves the list of commenter counts from the CommentService.
     *
     * @return The list of commenter counts.
     */
    public List<Object[]> getCommenterCounts() {
        return commentService.findCommenterCounts();
    }

    /**
     * Retrieves the word count map.
     *
     * @return The word count map.
     */
    public Map<String, Integer> getWordCount() {
        return wordCount;
    }

    /**
     * Kafka listener method to consume messages from "topicOne".
     *
     * @param message The JSON message containing comments.
     */
    @KafkaListener(topics = "topicOne", groupId = "groupCapybara")
    public void consume(String message) {
        LOGGER.info("Consuming message in KafkaConsumer1: {}", message);
        processMessage(message);
        printCommenterCount();
        printWordCount();

        //telegramBot.sendMessageToChat(commentService.findCommenterCounts(), wordCount);
    }

    /**
     * Process the JSON message, updating commenter and word counts.
     *
     * @param message The JSON message containing comments.
     */
    private void processMessage(String message) {
        LOGGER.info("Processing message in KafkaConsumer1: {}", message);
        JsonArray commentsArray = JsonParser.parseString(message).getAsJsonArray();
        for (JsonElement commentElement : commentsArray) {
            JsonObject commentObject = commentElement.getAsJsonObject();
            String commenter = commentObject.getAsJsonObject("user").get("login").getAsString();
            String commentBody = commentObject.get("body").getAsString();

            // Truncate the comment if it exceeds a certain length
            int maxCommentLength = 255; // Adjust the length as needed
            if (commentBody.length() > maxCommentLength) {
                commentBody = commentBody.substring(0, maxCommentLength);
            }

            // Create a new Comment entity and save it to the database
            Comment comment = new Comment(null, commenter, commentBody, wordCount.size());
            commentService.saveComment(comment);

            commenterCount.put(commenter, commenterCount.getOrDefault(commenter, 0) + 1);

            Set<String> stopWords = Set.of("a", "on", "in", "the", "and", "or", "but"); // Add more stop words as needed

            for (String word : commentBody.split("\\W+")) {
                if (!stopWords.contains(word.toLowerCase())) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }
    }

    /**
     * Print the list of active commenters with their comment counts.
     */
    private void printCommenterCount() {
        System.out.println();
        LOGGER.info("List of active commenters:");
        List<Object[]> commenterCounts = commentService.findCommenterCounts();
        commenterCounts.forEach(row -> LOGGER.info(row[0] + " [" + row[1] + " comments]"));
    }

    /**
     * Print the list of word counts in descending order.
     */
    private void printWordCount() {
        System.out.println();
        LOGGER.info("List of word count:");
        wordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> LOGGER.info(entry.getKey() + " [" + entry.getValue() + " times]"));
    }

}
