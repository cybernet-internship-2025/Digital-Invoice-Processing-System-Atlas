package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper
public interface InvoiceViewMapper {
    List<Invoice> getSentInvoicesById(@Param("id") UUID id);
    List<Invoice> getReceivedInvoicesById(@Param("id") UUID id);
    List<Invoice> getAllDraftsById(@Param("id") UUID id);
    List<Invoice> getAllInvoicesById(@Param("id") UUID id);
}
