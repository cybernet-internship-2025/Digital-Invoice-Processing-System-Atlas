package az.cybernet.auth.service;

public interface OTPService {

    String sendOTP(String phone);

    boolean verifyOTP(String phone, String otp);
}