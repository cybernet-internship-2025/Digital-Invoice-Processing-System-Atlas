package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.UserRequest;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponse> insertUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(service.insertUser(request));
    }
}
