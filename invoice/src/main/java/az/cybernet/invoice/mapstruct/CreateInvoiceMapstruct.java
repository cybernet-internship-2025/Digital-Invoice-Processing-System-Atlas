package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.ProductQuantityRequest;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CreateInvoiceMapstruct {

    Invoice toInvoice(CreateInvoiceRequest request);

    @Mapping(target = "invoiceId", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "quantity", source = "product.quantity")
    InvoiceProduct toInvoiceProduct(@MappingTarget UUID invoiceId, ProductQuantityRequest product);

    default List<InvoiceProduct> toInvoiceProductList(CreateInvoiceRequest request) {
        if (request == null || request.getProductQuantityRequests() == null) {
            return List.of();
        }
        return request.getProductQuantityRequests()
                .stream()
                .map(product -> toInvoiceProduct(request.getId(), product))
                .collect(Collectors.toList());
    }
}
