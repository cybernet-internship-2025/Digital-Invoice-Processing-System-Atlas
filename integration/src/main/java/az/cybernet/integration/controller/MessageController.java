package az.cybernet.integration.controller;

import az.cybernet.integration.dto.MessageRequest;
import az.cybernet.integration.service.SMSService;
import az.cybernet.integration.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/messaging")
public class MessageController {

    private final SMSService smsService;
    private final TelegramBotService telegramBotService;

    @PostMapping("/sendSMS")
    public ResponseEntity<String> sendSMS(@RequestBody MessageRequest request) {
        return ok(smsService.sendSMS(request.getPhone(), request.getMessage()));
    }

    @PostMapping("/sendTG")
    public ResponseEntity<String> sendTG(@RequestBody MessageRequest request) {
        return ok(telegramBotService.sendOTP(request.getPhone(), request.getMessage()));
    }
}
