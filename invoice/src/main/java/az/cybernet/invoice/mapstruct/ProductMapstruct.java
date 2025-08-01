package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.request.ProductQuantityRequest;
import az.cybernet.invoice.dto.request.ProductRequest;
import az.cybernet.invoice.dto.response.ProductResponse;
import az.cybernet.invoice.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductMapstruct {

    Product toEntity(ProductRequest productRequest);

    ProductResponse toDto(Product product);

    ProductRequest toProductRequest(ProductQuantityRequest request);

    default List<ProductRequest> toProductRequestList(List<ProductQuantityRequest> products) {
        if (products == null) {
            return List.of();
        }
        return products.stream()
                .map(this::toProductRequest)
                .toList();

    }
}
