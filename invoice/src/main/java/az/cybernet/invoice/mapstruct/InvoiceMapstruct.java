package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.dto.response.ProductDetailResponse;
import az.cybernet.invoice.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapstruct {

    Invoice toEntity(InvoiceRequest InvoiceRequest);

    InvoiceResponse toDto(Invoice invoice);

    InvoiceDetailResponse toDetailDto(InvoiceDetailed invoice);

    List<ProductDetailResponse> toProductDetailResponseList(List<InvoiceProduct> Invoiceproducts);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "invoiceId", source = "id")
    @Mapping(target = "timestamp", source = "updatedAt")
    InvoiceOperation invoiceToInvcOper(Invoice invoice);
}
