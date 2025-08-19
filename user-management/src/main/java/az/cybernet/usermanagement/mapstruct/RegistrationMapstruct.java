package az.cybernet.usermanagement.mapstruct;

import az.cybernet.usermanagement.dto.response.ApproveUserResponse;
import az.cybernet.usermanagement.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegistrationMapstruct {
    ApproveUserResponse toApprovalResponse(User user);

}
