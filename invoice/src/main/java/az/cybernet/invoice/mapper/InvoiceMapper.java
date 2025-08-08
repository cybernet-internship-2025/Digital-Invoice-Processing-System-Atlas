package az.cybernet.invoice.mapper;

import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceDetailed;
import az.cybernet.invoice.enums.InvoiceType;
import az.cybernet.invoice.enums.Status;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface InvoiceMapper {
    void changeInvoiceStatus(List<Invoice> invoices, Status status);

    void insertInvoice(Invoice invoice);

    Integer getLastInvoiceNumberOfMonth(@Param("startOfMonth") LocalDateTime start,
                                        @Param("startOfNextMonth") LocalDateTime end);

    Invoice sendBackForCorrection(@Param("id") UUID id, @Param("comment") String comment
            , @Param("updatedAt") LocalDateTime updatedAt);

    Invoice cancelInvoice(UUID id);

    Optional<InvoiceDetailed> getDetailedInvoice(@Param("invoiceId") UUID id);

    Optional<Invoice> findInvoiceById(@Param("id") UUID id);

    void approveInvoice(Invoice invoice);

    Invoice updateInvoice(@Param("id") UUID id,
                          @Param("status") Status status,
                          @Param("comment") String comment,
                          @Param("total") Double total,
                          @Param("updatedAt") LocalDateTime updatedAt);

    List<Invoice> findOldPendingInvoices(@Param("date") LocalDateTime date);

    List<FilteredInvoiceResp> filterInvoices(@Param("year") Integer year, @Param("fromDate") LocalDate fromDate
            , @Param("toDate") LocalDate toDate, @Param("status") Status status, @Param("series") String series
            , @Param("invoiceNumber") Integer invoiceNumber, @Param("type")InvoiceType type);

    List<Invoice> findPendingInvoicesUntil(@Param("date") LocalDateTime date);
}