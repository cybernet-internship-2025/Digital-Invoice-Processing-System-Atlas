package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InvoiceViewMapper {
    List<Invoice> getSentInvoicesByTaxId(@Param("taxId") String taxId);
    List<Invoice> getReceivedInvoicesByTaxId(@Param("taxId") String taxId);
    List<Invoice> getAllDraftsByTaxId(@Param("taxId") String taxId);
    List<Invoice> getAllInvoicesByTaxId(@Param("taxId") String taxId);
}
