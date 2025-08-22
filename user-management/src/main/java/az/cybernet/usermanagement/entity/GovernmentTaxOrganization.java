package az.cybernet.usermanagement.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GovernmentTaxOrganization {

    UUID id;
    String organizationName;
    Integer code;
}
