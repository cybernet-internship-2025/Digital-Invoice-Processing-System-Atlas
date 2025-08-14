package az.cybernet.invoice.mapper;

import az.cybernet.invoice.dto.request.InvoiceFilterRequest;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.entity.Invoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface InvoiceViewMapper {
    List<FilteredInvoiceResp> getSentInvoicesById(@Param("id") UUID id, @Param("req") InvoiceFilterRequest request
            , @Param("series") String series
            , @Param("invoiceNumber") Integer invoiceNumber);
    List<FilteredInvoiceResp> getReceivedInvoicesById(@Param("id") UUID id, @Param("req") InvoiceFilterRequest request
            , @Param("series") String series
            , @Param("invoiceNumber") Integer invoiceNumber);
    List<Invoice> getAllDraftsById(@Param("id") UUID id);
    List<Invoice> getAllInvoicesById(@Param("id") UUID id);
}
