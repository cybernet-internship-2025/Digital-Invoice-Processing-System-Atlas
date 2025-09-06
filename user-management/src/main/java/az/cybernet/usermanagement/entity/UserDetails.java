package az.cybernet.usermanagement.entity;

import az.cybernet.usermanagement.enums.RegistrationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserDetails {
    UUID id;
    String phoneNumber;
    String userId;
    String residentialAddress;
    String legalAddress;
    String legalEntityName;
    Timestamp registrationDate;
    RegistrationType registrationType;
    LocalDate dateOfBirth;
    UUID organizationId;
}