package az.cybernet.usermanagement.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Registration {
    String legalAddress;
    String residentialAddress;
    String phoneNumber;

    public Registration() {
        // Default constructor
    }

    public Registration(String legalAddress, String residentialAddress, String phoneNumber) {
        this.legalAddress = legalAddress;
        this.residentialAddress = residentialAddress;
        this.phoneNumber = phoneNumber;
    }
}
