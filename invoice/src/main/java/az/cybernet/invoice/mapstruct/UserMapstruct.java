package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.UserRequest;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapstruct {

    User toEntity(UserRequest userRequest);

    UserResponse toDto(User user);
}
