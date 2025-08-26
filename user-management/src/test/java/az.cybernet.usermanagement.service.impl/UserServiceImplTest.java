package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.dto.request.UserRequest;
import az.cybernet.usermanagement.dto.response.UserResponse;
import az.cybernet.usermanagement.entity.User;
import az.cybernet.usermanagement.exception.UserNotFoundException;
import az.cybernet.usermanagement.mapper.UserMapper;
import az.cybernet.usermanagement.mapstruct.UserMapstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserMapstruct mapstruct;

    @Test
    void insertUser_shouldSaveUserAndReturnResponse() {
        UUID id = UUID.randomUUID();
        String userName = "User001";
        UserRequest request = new UserRequest();
        request.setName(userName);

        User user = new User();
        user.setId(id);
        user.setName(userName);

        UserResponse expectedResponse = new UserResponse();

        when(mapstruct.toDto(any(User.class))).thenReturn(expectedResponse);

        UserResponse response = service.insertUser(request);

        verify(mapper).insertUser(any(User.class));
        assertEquals(expectedResponse, response);
    }

    @Test
    void getUserById_shouldReturnUserResponse_whenUserExists() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);

        when(mapper.findById(id)).thenReturn(Optional.of(user));
        when(mapstruct.toDto(user)).thenReturn(new UserResponse());

        UserResponse response = service.getUserById(id);

        assertNotNull(response);
        verify(mapper).findById(id);
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(mapper.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getUserById(id));
    }

    @Test
    void getAllUsers_shouldReturnListOfUserResponses() {
        List<User> userList = List.of(new User(), new User());

        when(mapper.findAllUsers()).thenReturn(userList);
        when(mapstruct.toList(userList)).thenReturn(List.of(new UserResponse(), new UserResponse()));

        List<UserResponse> responses = service.getAllUsers();

        assertEquals(2, responses.size());
        verify(mapper).findAllUsers();
    }
}
