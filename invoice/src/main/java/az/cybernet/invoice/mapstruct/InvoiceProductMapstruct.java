package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.response.InvoiceProductResponse;
import az.cybernet.invoice.entity.InvoiceProduct;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceProductMapstruct {

    InvoiceProduct toEntity(InvoiceProductRequest invoiceProductRequest);

    InvoiceProductResponse toDto(InvoiceProduct invoiceProduct);
}
