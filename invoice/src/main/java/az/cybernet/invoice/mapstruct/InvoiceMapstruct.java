package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapstruct {

    Invoice toEntity(InvoiceRequest InvoiceRequest);

    InvoiceResponse toDto(Invoice invoice);
}
