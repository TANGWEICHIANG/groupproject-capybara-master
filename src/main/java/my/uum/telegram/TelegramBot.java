package my.uum.telegram;

import my.uum.kafka.KafkaConsumer1;
import my.uum.kafka.KafkaConsumer2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final Map<Long, Process> userInputs = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBot.class);
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.chat.id}")
    private String chatId;

    @Autowired
    private KafkaConsumer1 kafkaConsumer1;
    @Autowired
    private KafkaConsumer2 kafkaConsumer2;

    public void setKafkaConsumer1(KafkaConsumer1 kafkaConsumer1) {
        this.kafkaConsumer1 = kafkaConsumer1;
    }

    public void setKafkaConsumer2(KafkaConsumer2 kafkaConsumer2) {
        this.kafkaConsumer2 = kafkaConsumer2;
    }

    public TelegramBot() {
        super("6653525503:AAFwvP9r5849uF-X-XvWjIyTpY8hYSbQZLw");
    }
    public TelegramBot(@Value("${telegram.bot.token}") String botToken,
                       @Value("${telegram.chat.id}") String chatId,
                       KafkaConsumer1 kafkaConsumer1,
                       KafkaConsumer2 kafkaConsumer2) {
        super(botToken);
        this.botToken = botToken;
        this.chatId = chatId;
        this.kafkaConsumer1 = kafkaConsumer1;
        this.kafkaConsumer2 = kafkaConsumer2;

        LOGGER.info("Bot Token: {}", botToken);
        LOGGER.info("Chat ID: {}", chatId);
    }


    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("Received update: {}", update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();

            if (message.trim().equalsIgnoreCase("/start")) {
                sendCommands(chatId);
            } else if (message.trim().equalsIgnoreCase("/consumer")) {

                sendMessageToChat(kafkaConsumer1.getCommenterCounts(), kafkaConsumer1.getWordCount());
                sendMessageToChat(kafkaConsumer2.getCommenterCounts(), kafkaConsumer2.getWordCount());
            } else if (message.trim().equalsIgnoreCase("/quit")) {
                sendTerminateMessage(chatId);
                System.exit(0);
            } else {
                sendNoResultMessage(chatId);
            }
        }
    }

    private void sendTerminateMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Bot Terminate Successfully");

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error sending message to Telegram", e);
        }
    }

    public void sendMessageToChat(List<Object[]> commenterCounts, Map<String, Integer> wordCount) {
        // Call the existing method with the required data
        sendMessageToChat(formatMessage(commenterCounts, wordCount));
    }

    private void sendNoResultMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("No result printed");

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error sending message to Telegram", e);
        }
    }

    // Helper method to send a long message (truncated or split)
    private void sendLongMessage(String longMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(longMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error sending message to Telegram: {}", e.getMessage());
        }
    }

    // Modify the existing sendMessageToChat method to call the new method
    public void sendMessageToChat(String message) {
        LOGGER.info("Sending message to chat: {}", message);

        // Set a maximum message length to avoid "message is too long" error
        int maxMessageLength = 4096;  // Adjust the length as needed

        if (message.length() > maxMessageLength) {
            // Message is too long, truncate or split it
            sendLongMessage(message.substring(0, maxMessageLength));
        } else {
            // Message is within the allowed length, send it as is
            sendLongMessage(message);
        }
    }

    // Format and append the list of active commenters and word count
    private String formatMessage(List<Object[]> commenterCounts, Map<String, Integer> wordCount) {
        StringBuilder message = new StringBuilder();

        // Format and append the list of active commenters
        message.append("List of active commenters:\n");
        commenterCounts.stream()
                .sorted((a, b) -> Long.compare((Long) b[1], (Long) a[1])) // Sort by comment count descending
                .forEach(row -> message.append(String.format("%s [%d comments]\n", row[0], row[1])));

        // Add a newline for separation
        message.append("\n");

        // Format and append the list of word count
        message.append("List of word count:\n");
        wordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> message.append(String.format("%s [%d times]\n", entry.getKey(), entry.getValue())));

        return message.toString();
    }

    private void sendCommands(Long chatId) {
        String commandsMessage = "Welcome to Capybara Bot\nAvailable commands are shown in below:\n\n" +
                "1) /consumer         - Display word count.\n" +
                "2) /quit             - Terminate bot.\n";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(commandsMessage);

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        // Return your bot's username
        return "A231_STIW3054_capybara_bot";
    }

    @Override
    public String getBotToken() {
        // Return your bot's token
        return "6653525503:AAFwvP9r5849uF-X-XvWjIyTpY8hYSbQZLw";
    }

}

