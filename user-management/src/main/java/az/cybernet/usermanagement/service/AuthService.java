package az.cybernet.usermanagement.service;

public interface AuthService {
    String loginSendOTP(String pin, String phoneNumber);
    String verifyLoginOTP(String pin, String phoneNumber, String otp);
}
