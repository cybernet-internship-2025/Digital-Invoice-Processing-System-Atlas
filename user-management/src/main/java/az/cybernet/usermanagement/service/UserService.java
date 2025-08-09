package az.cybernet.usermanagement.service;

import az.cybernet.usermanagement.dto.UserRequest;
import az.cybernet.usermanagement.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse insertUser(UserRequest request);
    UserResponse getUserById(UUID id);
    List<UserResponse> getAllUsers();
    UserResponse getUserByTaxId(String taxId);
}
