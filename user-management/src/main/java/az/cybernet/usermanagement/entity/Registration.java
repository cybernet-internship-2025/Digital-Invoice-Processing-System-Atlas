package az.cybernet.usermanagement.entity;

import az.cybernet.usermanagement.enums.RegistrationStatus;
import az.cybernet.usermanagement.enums.RegistrationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Registration {
    String legalAddress;
    String residentialAddress;
    String phoneNumber;
    String legalEntityName;
    RegistrationType typeOfRegistration;
    String registrationNumber;
    RegistrationStatus registrationStatus;
}
