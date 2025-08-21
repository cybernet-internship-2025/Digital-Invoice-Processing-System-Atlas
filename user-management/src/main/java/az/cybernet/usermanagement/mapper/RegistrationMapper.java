package az.cybernet.usermanagement.mapper;

import az.cybernet.usermanagement.entity.Registration;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegistrationMapper {
    void insertRegistration(Registration registration);
}
