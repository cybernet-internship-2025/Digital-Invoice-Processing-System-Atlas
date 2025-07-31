package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.InvoiceProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface InvoiceProductMapper {

    void insertInvoiceProduct(InvoiceProduct invoiceProduct);
    List<InvoiceProduct> findActiveByInvoiceId(@Param("invoiceId") UUID invoiceId);
}
