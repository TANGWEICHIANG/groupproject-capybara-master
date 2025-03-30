/**
 * This class represents a Spring Boot application for Kafka integration.
 * The application utilizes Kafka producers to fetch GitHub issues' comments
 * and send them to Kafka topics ("topicOne" and "topicTwo").
 *
 * This class is annotated with @SpringBootApplication to enable Spring Boot
 * auto-configuration and component scanning.
 *
 * Usage example:
 * 1. Run the Spring Boot application to fetch GitHub issues' comments and
 *    send them to Kafka topics.
 *
 * Note: Ensure that Kafka brokers are running and configured properly.
 *
 * @author group Capybara
 * @version 1.0
 * @since 2024-01-21
 */
package my.uum;

import my.uum.kafka.KafkaProducer1;
import my.uum.kafka.KafkaProducer2;
import my.uum.telegram.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import java.net.URISyntaxException;

@SpringBootApplication
public class SpringbootKafka {

    /**
     * The main method to start the Spring Boot application.
     *
     * @param args Command-line arguments.
     * @throws URISyntaxException If the URL is not a valid URI.
     */
    public static void main(String[] args) throws URISyntaxException, TelegramApiException {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootKafka.class, args);

        KafkaProducer1 kafkaProducer1 = context.getBean(KafkaProducer1.class);
        kafkaProducer1.sendMessage("https://api.github.com/repos/TANGWEICHIANG/KafkaAPITest/issues/1/comments");

        KafkaProducer2 kafkaProducer2 = context.getBean(KafkaProducer2.class);
        kafkaProducer2.sendMessage("https://api.github.com/repos/pavlovcik/ubiquibot/issues/30/comments");

        TelegramBot bot = context.getBean(TelegramBot.class);

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

}
