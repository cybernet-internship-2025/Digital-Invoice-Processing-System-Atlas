package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface InvoiceMapper {

    void insertInvoice(Invoice invoice);
    int getNextInvoiceNum();

    Optional<Invoice> findInvoiceDetailsById(@Param("invoiceId") UUID invoiceId);
}