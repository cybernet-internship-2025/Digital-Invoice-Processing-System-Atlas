package az.cybernet.usermanagement.service;

public interface AuthService {
    String loginSendOTP(String pin, String phoneNumber);
}
