package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.UserRequest;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.User;
import az.cybernet.invoice.mapper.UserMapper;
import az.cybernet.invoice.mapstruct.UserMapstruct;
import az.cybernet.invoice.service.UserService;
import org.springframework.stereotype.Service;

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
        User user = mapstruct.toEntity(request);

        mapper.insertUser(user);

        return mapstruct.toDto(user);
    }

//    @Override
//    public UserResponse insertUser(UserRequest request) {
//        User user = new User();
//        user.setId(request.getId());
//        user.setName(request.getName());
//        user.setTaxId(request.getTaxId());
//        System.out.println("Mapped User entity: " + user);
//
//        mapper.insertUser(user);
//
//        UserResponse response = new UserResponse();
//        response.setId(user.getId());
//        response.setName(user.getName());
//        response.setTaxId(user.getTaxId());
//        System.out.println("Mapped UserResponse entity: " + response);
//
//        return response;
//    }
}
