package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.InvoiceOperationRequest;
import az.cybernet.invoice.dto.response.InvoiceOperationResponse;
import az.cybernet.invoice.entity.InvoiceOperation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceOperationMapstruct {

    InvoiceOperation toEntity(InvoiceOperationRequest invoiceOperationRequest);

    InvoiceOperationResponse toDto(InvoiceOperation invoiceOperation);
}
