package az.cybernet.usermanagement.service;

public interface OTPService {
    String sendOTP(String phone);
    boolean verifyOTP(String phone, String otp);
}
