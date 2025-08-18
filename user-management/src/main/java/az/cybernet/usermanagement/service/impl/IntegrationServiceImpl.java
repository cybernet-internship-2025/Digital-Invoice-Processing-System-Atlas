package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.client.IntegrationClient;
import az.cybernet.usermanagement.dto.response.IamasDto;
import az.cybernet.usermanagement.exception.PinDataNotFoundException;
import az.cybernet.usermanagement.service.IntegrationService;
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
}
