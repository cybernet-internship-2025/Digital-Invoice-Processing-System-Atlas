package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.dto.UserRequest;
import az.cybernet.usermanagement.dto.UserResponse;
import az.cybernet.usermanagement.entity.User;
import az.cybernet.usermanagement.exception.UserNotFoundException;
import az.cybernet.usermanagement.mapper.UserMapper;
import az.cybernet.usermanagement.mapstruct.UserMapstruct;
import az.cybernet.usermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private final UserMapstruct mapstruct;

    public UserServiceImpl(UserMapper mapper, UserMapstruct mapstruct) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
    }

    @Override
    public UserResponse insertUser(UserRequest request) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setTaxId(request.getTaxId());

        mapper.insertUser(user);

        return mapstruct.toDto(user);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User user = mapper.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return mapstruct.toDto(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> userList = mapper.findAllUsers();
        return mapstruct.toList(userList);
    }
}
