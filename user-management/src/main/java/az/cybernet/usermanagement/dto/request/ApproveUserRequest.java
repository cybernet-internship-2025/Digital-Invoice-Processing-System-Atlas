package az.cybernet.usermanagement.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApproveUserRequest {

    UUID userId;
    LocalDate dateOfBirth;
}