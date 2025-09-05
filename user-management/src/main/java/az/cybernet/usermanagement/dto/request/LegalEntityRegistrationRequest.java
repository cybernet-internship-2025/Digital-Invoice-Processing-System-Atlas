package az.cybernet.usermanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LegalEntityRegistrationRequest {

    @NotNull
    UUID userId;

    @NotNull
    String legalAddress;

    @NotNull
    String residentialAddress;

    @NotNull
    @Pattern(regexp = "^(?:\\+994|994)(?:50|51|55|60|70|77|99)\\d{7}$")
    String phoneNumber;

    @NotNull
    String legalEntityName;

    @NotNull
    UUID organizationId;
}
