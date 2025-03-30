package my.uum.config;

import jakarta.annotation.PostConstruct;
import my.uum.kafka.KafkaConsumer1;
import my.uum.kafka.KafkaConsumer2;
import my.uum.telegram.TelegramBot;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for Kafka topics and related components.
 * Manages the creation of Kafka topics and initializes the relationships
 * between Kafka consumers and a Telegram bot.
 *
 * @author group Capybara
 * @version 1.0
 * @since 2024-01-21
 */
@Configuration
public class KafkaTopicConfig {

    /**
     * Creates and returns a NewTopic bean for "topicOne".
     *
     * @return A NewTopic bean for "topicOne".
     */
    @Bean
    public NewTopic createTopic1() {
        return TopicBuilder.name("topicOne").build();
    }

    /**
     * Creates and returns a NewTopic bean for "topicTwo".
     *
     * @return A NewTopic bean for "topicTwo".
     */
    @Bean
    public NewTopic createTopic2() {
        return TopicBuilder.name("topicTwo").build();
    }

    @Autowired
    private KafkaConsumer1 kafkaConsumer1;

    @Autowired
    private KafkaConsumer2 kafkaConsumer2;

    @Autowired
    private TelegramBot telegramBot;

    /**
     * Initializes relationships between Kafka consumers and a Telegram bot
     * after the Spring context has been constructed.
     * <p>
     * This method sets up the communication between KafkaConsumer1, KafkaConsumer2,
     * and the TelegramBot by establishing the necessary references.
     */
    @PostConstruct
    public void init() {
        kafkaConsumer1.setTelegramBot(telegramBot);
        telegramBot.setKafkaConsumer1(kafkaConsumer1);

        kafkaConsumer2.setTelegramBot(telegramBot);
        telegramBot.setKafkaConsumer2(kafkaConsumer2);
    }
}
