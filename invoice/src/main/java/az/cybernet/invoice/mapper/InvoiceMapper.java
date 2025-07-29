package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.enums.Status;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper
public interface InvoiceMapper {
    void changeInvoiceStatus(List<Invoice> invoices, Status status);

    void insertInvoice(Invoice invoice);

    int getNextInvoiceNum();

    Invoice sendBackForCorrection(@Param("id") UUID id, @Param("comment") String comment
            , @Param("updatedAt") LocalDateTime updatedAt);

    Invoice findInvoiceById(@Param("id") UUID id);
}