package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceDetailed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface InvoiceMapper {
    void insertInvoice(Invoice invoice);

    int getNextInvoiceNum();

    Optional<InvoiceDetailed> getDetailedInvoice(@Param("invoiceId") UUID id);
}