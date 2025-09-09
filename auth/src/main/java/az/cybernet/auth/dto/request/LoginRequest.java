package az.cybernet.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotBlank
    @Size(min = 7, max = 7, message = "PIN must be exactly 7 characters")
    String pin;

    @Pattern(
            regexp = "^\\(10|50|51|55|70|77|99)[0-9]{7}$",
            message = "Phone number must be a valid number like 505555555"
    )
    String phoneNumber;
}