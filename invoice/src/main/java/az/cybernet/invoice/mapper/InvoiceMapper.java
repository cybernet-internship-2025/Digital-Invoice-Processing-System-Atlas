package az.cybernet.invoice.mapper;

import az.cybernet.invoice.dto.response.ProductDetailResponse;
import az.cybernet.invoice.entity.Invoice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper
public interface InvoiceMapper {

    List<ProductDetailResponse> getInvoiceProductListById(UUID id);

    void insertInvoice(Invoice invoice);
    int getNextInvoiceNum();
    Invoice findInvoiceDetailsById(UUID id);
}