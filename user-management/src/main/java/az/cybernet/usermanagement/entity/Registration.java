package az.cybernet.usermanagement.entity;

import az.cybernet.usermanagement.enums.RegistrationStatus;
import az.cybernet.usermanagement.enums.RegistrationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Registration {
    UUID id;
    UUID userId;
    String legalAddress;
    String residentialAddress;
    String phoneNumber;
    String legalEntityName;
    RegistrationType typeOfRegistration;
    String registrationNumber;
    RegistrationStatus registrationStatus;
    Timestamp registrationDate;
    UUID organizationId;
}
