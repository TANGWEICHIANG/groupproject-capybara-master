package my.uum.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import my.uum.github.GitHubApiClient;

import java.net.URISyntaxException;

/**
 * Kafka Producer service for sending messages to "topicTwo".
 * Fetches GitHub issues using the GitHubApiClient and sends the JSON message to the Kafka topic.
 * <p>
 * This class is annotated with @Service to indicate that it is a Spring-managed service bean.
 * Utilizes the KafkaTemplate for sending messages to the Kafka topic.
 *
 * @author group Capybara
 * @version 1.0
 * @since 2024-01-21
 */
@Service
public class KafkaProducer2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer2.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Constructor for KafkaProducer2.
     *
     * @param kafkaTemplate The KafkaTemplate for sending messages.
     */
    public KafkaProducer2(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Use kafka template to send a message to "topicTwo".
     *
     * @param url The URL to fetch GitHub issues from.
     * @throws URISyntaxException If the URL is not a valid URI.
     */
    public void sendMessage(String url) throws URISyntaxException {
        String issuesJson2 = GitHubApiClient.fetchGitHubIssues2(url);
        System.out.println();
        LOGGER.info(String.format("Message sent %s", issuesJson2));
        kafkaTemplate.send("topicTwo", issuesJson2);
    }
}
