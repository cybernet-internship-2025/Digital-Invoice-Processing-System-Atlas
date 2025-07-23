package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.UserRequest;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.User;
import az.cybernet.invoice.mapper.UserMapper;
import az.cybernet.invoice.mapstruct.UserMapstruct;
import az.cybernet.invoice.service.UserService;
import org.springframework.stereotype.Service;

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
        User user = mapstruct.toEntity(request);
        mapper.insertUser(user);
        return mapstruct.toDto(user);
    }
}
