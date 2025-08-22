package az.cybernet.integration.controller;

import az.cybernet.integration.dto.SMSRequest;
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

    @PostMapping("/send")
    public ResponseEntity<String> sendSMS(@RequestBody SMSRequest request) {
        return ok(smsService.sendSMS(request.getPhone(), request.getMessage()));
    }
}
