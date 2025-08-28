package az.cybernet.usermanagement.mapstruct;

import az.cybernet.usermanagement.entity.Registration;
import az.cybernet.usermanagement.entity.User;
import az.cybernet.usermanagement.entity.UserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDetailsMapstruct {
    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "registration.phoneNumber", target = "phoneNumber")
    @Mapping(source = "registration.residentialAddress", target = "residentialAddress")
    @Mapping(source = "registration.legalAddress", target = "legalAddress")
    @Mapping(source = "registration.legalEntityName", target = "legalEntityName")
    @Mapping(source = "registration.registrationDate", target = "registrationDate")
    @Mapping(source = "registration.typeOfRegistration", target = "registrationType")
    UserDetails userToUserDetails(User user, Registration registration);
}
