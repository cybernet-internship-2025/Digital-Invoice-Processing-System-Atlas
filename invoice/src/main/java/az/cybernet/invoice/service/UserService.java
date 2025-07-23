package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.UserRequest;
import az.cybernet.invoice.dto.response.UserResponse;

public interface UserService {

    UserResponse insertUser(UserRequest request);
}
