package az.cybernet.usermanagement.mapper;

import az.cybernet.usermanagement.entity.User;
import az.cybernet.usermanagement.entity.Registration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

@Mapper
public interface RegistrationMapper {
    User findById(@Param("id") UUID id);
    void updateUser(User user);
    void insertRegistration(Registration registration);
}
