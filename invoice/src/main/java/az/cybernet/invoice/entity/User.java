package az.cybernet.invoice.entity;

import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    int id;
    String name;
    String taxId;
}
