package az.cybernet.auth.service.impl;

import az.cybernet.auth.client.UserClient;
import az.cybernet.auth.config.JwtService;
import az.cybernet.auth.dto.response.UserInfoResponse;
import az.cybernet.auth.exception.InvalidOtpException;
import az.cybernet.auth.exception.PhoneNumberNotLinkedException;
import az.cybernet.auth.service.AuthService;
import az.cybernet.auth.service.IntegrationService;
import az.cybernet.auth.service.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final IntegrationService integrationService;
    private final JwtService jwtService;
    private final OTPService otpService;
    private final UserClient userClient;

    @Override
    public String loginSendOTP(String pin, String phoneNumber) {
        var pinData = integrationService.getPinData(pin);

        boolean phoneExists = Optional.ofNullable(pinData.getPhoneNumbers())
                .orElse(List.of())
                .contains(phoneNumber);

        if (!phoneExists) {
            throw new PhoneNumberNotLinkedException(
                    "Phone number " + phoneNumber + " is not linked with pin: " + pin
            );
        }

        return otpService.sendOTP(phoneNumber);

//        return "OTP sent to user";
    }

    @Override
    public String verifyLoginOTP(String pin, String phoneNumber, String otp) {
        if (otpService.verifyOTP(phoneNumber, otp)) {
            return jwtService.generateToken(pin, phoneNumber, List.of("ADMIN"));
        }

        throw new InvalidOtpException("Invalid or expired OTP");
    }
}