package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.request.ProductQuantityRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface InvoiceMapstruct {

    Invoice toEntity(InvoiceRequest InvoiceRequest);

    InvoiceResponse toDto(Invoice invoice);

    InvoiceRequest getInvoiceFromCreateRequest(CreateInvoiceRequest request);

    @Mapping(target = "invoiceId", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "quantity", source = "product.quantity")
    InvoiceProductRequest toInvoiceProductRequest(@MappingTarget UUID invoiceId, ProductQuantityRequest product);

    default List<InvoiceProductRequest> toInvoiceProductRequestList(CreateInvoiceRequest request) {
        if (request == null || request.getProductQuantityRequests() == null) {
            return List.of();
        }
        return request.getProductQuantityRequests()
                .stream()
                .map(product -> toInvoiceProductRequest(request.getId(), product))
                .collect(Collectors.toList());
    }
}
