package az.cybernet.usermanagement.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    UUID id;
    String name;
    String taxId;
}
