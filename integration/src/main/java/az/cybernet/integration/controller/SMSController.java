package az.cybernet.integration.controller;

import az.cybernet.integration.service.SMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sms")
public class SMSController {

    private final SMSService smsService;

    @PostMapping("/send/{phone}")
    public ResponseEntity<String> sendSMS(@PathVariable("phone") String phone, @RequestBody String message) {
        return ok(smsService.sendSMS(phone, message));
    }
}
