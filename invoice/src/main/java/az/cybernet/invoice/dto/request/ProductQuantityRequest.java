package az.cybernet.invoice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductQuantityRequest{
    UUID id;
    String name;
    Double price;
    UUID measurementId;
    Double quantity;
}
