package az.cybernet.invoice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Product {
    int id;
    String name;
    double price;
    int measurementId;
}
