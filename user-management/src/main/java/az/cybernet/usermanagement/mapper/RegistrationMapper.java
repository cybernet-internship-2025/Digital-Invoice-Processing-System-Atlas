package az.cybernet.usermanagement.mapper;

import az.cybernet.usermanagement.entity.Registration;
import az.cybernet.usermanagement.enums.RegistrationStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface RegistrationMapper {
    void insertRegistration(Registration registration);

    Optional<Registration> findPendingRegistrationByUserId(@Param("userId") UUID userId);

    Optional<Registration> findPendingRegistrationById(@Param("id") UUID id);

    void updateStatus(@Param("registrationId") UUID registrationId, @Param("status") RegistrationStatus status);
}
