package az.cybernet.usermanagement.mapstruct;

import az.cybernet.usermanagement.dto.response.UserResponse;
import az.cybernet.usermanagement.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapstruct {
    UserResponse toDto(User user);
    List<UserResponse> toList(List<User> users);
}
