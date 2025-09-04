package az.cybernet.auth.service.impl;

import az.cybernet.auth.client.IntegrationClient;
import az.cybernet.auth.dto.request.SMSRequest;
import az.cybernet.auth.dto.response.IamasDto;
import az.cybernet.auth.exception.FailedToSendSMSException;
import az.cybernet.auth.exception.PinDataNotFoundException;
import az.cybernet.auth.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService {

    private final IntegrationClient integrationClient;

    @Override
    public IamasDto getPinData(String pin) {
        return Optional.ofNullable(integrationClient.getPinData(pin))
                .orElseThrow(() -> new PinDataNotFoundException("Data not found for pin: " + pin));
    }

    @Override
    public String sendSMS(String phone, String message) {
        return Optional.ofNullable(integrationClient.sendSMS(new SMSRequest(phone, message)))
                .orElseThrow(() -> new FailedToSendSMSException("Failed to send sms to " + phone));
    }
}