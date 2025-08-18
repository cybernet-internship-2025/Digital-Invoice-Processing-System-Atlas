package az.cybernet.usermanagement.service;

import az.cybernet.usermanagement.dto.response.IamasDto;

public interface IntegrationService {

    IamasDto getPinData(String pin);
}
