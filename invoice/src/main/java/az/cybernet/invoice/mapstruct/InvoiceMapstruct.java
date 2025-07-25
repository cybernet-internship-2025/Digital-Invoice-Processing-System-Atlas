package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.*;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface InvoiceMapstruct {

    Invoice toEntity(InvoiceRequest InvoiceRequest);

    InvoiceResponse toDto(Invoice invoice);

    InvoiceRequest getInvoiceFromCreateRequest(CreateInvoiceRequest request);

    @Mapping(target = "invoiceId", source = "invoiceId")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "quantity", source = "product.quantity")
    InvoiceProductRequest toInvoiceProductRequest(UUID invoiceId, ProductQuantityRequest product);

    default List<InvoiceProductRequest> toInvoiceProductRequestList(CreateInvoiceRequest request) {
        if (request == null || request.getProductQuantityRequests() == null) {
            return List.of();
        }
        return request.getProductQuantityRequests()
                .stream()
                .map(product -> toInvoiceProductRequest(request.getId(), product))
                .toList();
    }

    ProductRequest toProductRequest(ProductQuantityRequest request);

    default List<ProductRequest> toProductRequestList(CreateInvoiceRequest request) {
        if (request == null || request.getProductQuantityRequests() == null) {
            return List.of();
        }
        return request.getProductQuantityRequests()
                .stream()
                .map(this::toProductRequest)
                .toList();
    }

}
