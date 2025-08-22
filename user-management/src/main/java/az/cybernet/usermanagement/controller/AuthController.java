package az.cybernet.usermanagement.controller;

import az.cybernet.usermanagement.dto.request.LoginReq;
import az.cybernet.usermanagement.dto.request.OtpVerificationRequest;
import az.cybernet.usermanagement.dto.request.SMSRequest;
import az.cybernet.usermanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> loginWithPinAndPhone(@RequestBody LoginReq request) {
        String response = authService.loginSendOTP(request.getPin(), request.getPhoneNumber());
        return ok(response);
    }

    @PostMapping("/login/verification")
    public ResponseEntity<String> loginWithPinAndPhone(@RequestBody OtpVerificationRequest request) {
        String response = authService.verifyLoginOTP(request.getPin(), request.getPhoneNumber(), request.getOtp());
        return ok(response);
    }
}
