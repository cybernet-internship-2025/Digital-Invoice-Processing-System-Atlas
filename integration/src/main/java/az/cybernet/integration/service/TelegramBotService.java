package az.cybernet.integration.service;

import az.cybernet.integration.dto.ChatDTO;
import az.cybernet.integration.mybatis.ChatMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUserame;
    private final ChatMapper chatMapper;

    public TelegramBotService(@Value("${telegram.bot.token}") String botToken,
                              @Value("${telegram.bot.username}") String botUserame,
                              ChatMapper chatMapper) {
        this.botToken = botToken;
        this.botUserame = botUserame;
        this.chatMapper = chatMapper;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();

        if(text.equals("/start")) {
            sendStartMessage(chatId);
        } else if(text.matches("\\d{9}$")) {
            insertChat(chatId, text);
        }
    }

    public void sendStartMessage(String chatId) {
        String startMessage = "Please enter your phone number in the following form: 012345678.";
        SendMessage sendMessage = new SendMessage(chatId, startMessage);
        try{
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void insertChat(String chatId, String phone) {
        ChatDTO chat = new ChatDTO(chatId, phone);
        chatMapper.insertChat(chat);

        String message = "User remembered, now you will receive OTP for phone number: " + phone;
        SendMessage sendMessage = new SendMessage(chatId, message);
        try{
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String sendOTP(String phone, String otp) {
        List<String> chatIds = chatMapper.findChatIdByPhone(phone);

        for (String chatId : chatIds) {
            SendMessage sendMessage = new SendMessage(chatId, otp);
            try {
                this.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return "Otp sent successfully";
    }

    @Override
    public String getBotUsername() {
        return botUserame;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
