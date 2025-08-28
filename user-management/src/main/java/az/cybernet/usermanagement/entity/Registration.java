package az.cybernet.usermanagement.entity;

import az.cybernet.usermanagement.enums.RegistrationStatus;
import az.cybernet.usermanagement.enums.RegistrationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Registration {
    private UUID id;
    private UUID userId;
    private UUID governmentTaxOrganizationId;
    String legalAddress;
    String residentialAddress;
    String phoneNumber;
    String legalEntityName;
    RegistrationType typeOfRegistration;
    String registrationNumber;
    RegistrationStatus registrationStatus;
}
