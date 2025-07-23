package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvoiceMapper {

    void insertInvoice(Invoice invoice);
}
