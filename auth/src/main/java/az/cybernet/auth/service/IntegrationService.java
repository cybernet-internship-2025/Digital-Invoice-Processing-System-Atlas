package az.cybernet.auth.service;

import az.cybernet.auth.dto.response.IamasDto;

public interface IntegrationService {

    IamasDto getPinData(String pin);

    String sendSMS(String phone, String message);
}