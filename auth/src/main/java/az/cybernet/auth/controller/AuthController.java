package az.cybernet.auth.controller;


import az.cybernet.auth.dto.request.LoginRequest;
import az.cybernet.auth.dto.request.OtpVerificationRequest;
import az.cybernet.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/authorization")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> loginWithPinAndPhone(@RequestBody LoginRequest request) {
        String response = authService.loginSendOTP(request.getPin(), request.getPhoneNumber());
        return ok(response);
    }

    @PostMapping("/login/verification")
    public ResponseEntity<String> loginWithPinAndPhone(@RequestBody OtpVerificationRequest request) {
        String response = authService.verifyLoginOTP(request.getPin(), request.getPhoneNumber(), request.getOtp());
        return ok(response);
    }
}