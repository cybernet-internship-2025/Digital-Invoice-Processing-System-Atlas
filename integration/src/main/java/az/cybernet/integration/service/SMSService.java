package az.cybernet.integration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SMSService {

    public String sendSMS(String phone, String message) {
        return "Sent SMS message with content \"" +
                message + "\" to " + phone;
    }

}
