package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.config.JwtService;
import az.cybernet.usermanagement.exception.InvalidOtpException;
import az.cybernet.usermanagement.exception.PhoneNumberNotLinkedException;
import az.cybernet.usermanagement.service.AuthService;
import az.cybernet.usermanagement.service.IntegrationService;
import az.cybernet.usermanagement.service.OTPService;
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

        otpService.sendOTP(phoneNumber);

        return "OTP sent to user";
    }

    @Override
    public String verifyLoginOTP(String pin, String phoneNumber, String otp) {
        if (otpService.verifyOTP(phoneNumber, otp)) {
            return jwtService.generateToken(pin, phoneNumber);
        }

        throw new InvalidOtpException("Invalid or expired OTP");
    }
}
