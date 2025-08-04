package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.request.ProductQuantityRequest;
import az.cybernet.invoice.dto.response.InvoiceProductResponse;
import az.cybernet.invoice.entity.InvoiceProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface InvoiceProductMapstruct {

    InvoiceProduct toEntity(InvoiceProductRequest invoiceProductRequest);

    InvoiceProductResponse toDto(InvoiceProduct invoiceProduct);

    @Mapping(target = "invoiceId", source = "invoiceId")
    @Mapping(target = "quantity", source = "product.quantity")
    @Mapping(target = "productId", source = "product.id")
    InvoiceProductRequest toInvoiceProductRequest(UUID invoiceId, ProductQuantityRequest product);

    default List<InvoiceProductRequest> toInvoiceProductRequestList(UUID invoiceId, List<ProductQuantityRequest> products) {
        if (products == null) {
            return List.of();
        }
        return products.stream()
                .map(product -> toInvoiceProductRequest(invoiceId, product))
                .toList();

    }
}