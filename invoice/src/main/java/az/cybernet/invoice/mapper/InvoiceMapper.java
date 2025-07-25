package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.enums.Status;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface InvoiceMapper {
    void changeInvoiceStatus(List<Invoice> invoices, Status status);

    void insertInvoice(Invoice invoice);
    int getNextInvoiceNum();

    Invoice cancelInvoice(@Param("id") UUID id);
}