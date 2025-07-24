package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.InvoiceProduct;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvoiceProductMapper {

    void insertInvoiceProduct(InvoiceProduct invoiceProduct);
}
