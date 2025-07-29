package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.*;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceOperation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapstruct {

    Invoice toEntity(InvoiceRequest InvoiceRequest);

    InvoiceResponse toDto(Invoice invoice);

    InvoiceRequest getInvoiceFromCreateRequest(CreateInvoiceRequest request);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "invoiceId", source = "id")
    @Mapping(target = "timestamp", source = "updatedAt")
    InvoiceOperation invoiceToInvcOper(Invoice invoice);
}
