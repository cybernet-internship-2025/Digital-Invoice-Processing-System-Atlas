package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.dto.response.ProductDetailResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceDetailed;
import az.cybernet.invoice.entity.InvoiceProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapstruct {

    Invoice toEntity(InvoiceRequest request);

    InvoiceResponse toDto(Invoice invoice);

    InvoiceDetailResponse toDetailDto(InvoiceDetailed invoice);

    List<ProductDetailResponse> toProductDetailResponseList(List<InvoiceProduct> invoiceProducts);

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.measurement.name", target = "measurementName")
    ProductDetailResponse toProductDetailResponse(InvoiceProduct invoiceProduct);
}