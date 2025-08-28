package az.cybernet.invoice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductDetailResponse {
    String productName;
    BigDecimal quantity;
    BigDecimal price;
    String measurementName;
}
