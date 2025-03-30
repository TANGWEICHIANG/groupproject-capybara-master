package my.uum.kafka;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import my.uum.entity.Comment2;
import my.uum.jpa.CommentService2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import my.uum.telegram.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Kafka Consumer service for processing messages from "topicTwo".
 * Handles the consumption of messages, processing and updating commenter and word counts,
 * and communicates with the TelegramBot to send the results to a chat.
 *
 * This class is annotated with @Service to indicate that it is a Spring-managed service bean.
 * Utilizes the CommentService2 for database operations and communicates with the TelegramBot.
 *
 * @author group Capybara
 * @version 1.0
 * @since 2024-01-21
 */
@Service
public class KafkaConsumer2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer2.class);
    private final Map<String, Integer> commenterCount = new HashMap<>();
    private final Map<String, Integer> wordCount = new HashMap<>();

    @Autowired
    private CommentService2 commentService2;

    private TelegramBot telegramBot;

    /**
     * Sets the TelegramBot instance for communication.
     *
     * @param telegramBot The TelegramBot instance.
     */
    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Retrieves the list of commenter counts from the CommentService2.
     *
     * @return The list of commenter counts.
     */
    public List<Object[]> getCommenterCounts() {
        return commentService2.findCommenterCounts();
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
     * Kafka listener method to consume messages from "topicTwo".
     *
     * @param message The JSON message containing comments.
     */
    @KafkaListener(topics = "topicTwo", groupId = "groupCapybara")
    public void consume(String message) throws TelegramApiException {
        processMessage(message);
        printCommenterCount();
        printWordCount();
        TelegramBot bot = new TelegramBot();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
//        telegramBot.sendMessageToChat(commentService2.findCommenterCounts(), wordCount);

    }

    /**
     * Process the JSON message, updating commenter and word counts.
     *
     * @param message The JSON message containing comments.
     */
    private void processMessage(String message) {
        LOGGER.info("Processing message in KafkaConsumer2: {}", message);
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

            // Create a new Comment2 entity and save it to the database
            Comment2 comment2 = new Comment2(null, commenter, commentBody, wordCount.size());
            commentService2.saveComment2(comment2);

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
        List<Object[]> commenterCounts = commentService2.findCommenterCounts();
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
