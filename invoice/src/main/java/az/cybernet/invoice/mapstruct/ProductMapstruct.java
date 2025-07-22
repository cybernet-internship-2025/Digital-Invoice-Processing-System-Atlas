package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.ProductRequest;
import az.cybernet.invoice.dto.response.ProductResponse;
import az.cybernet.invoice.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapstruct {

    Product toEntity(ProductRequest productRequest);

    ProductResponse toDto(Product product);
}
