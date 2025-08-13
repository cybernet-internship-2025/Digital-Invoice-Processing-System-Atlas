package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper
public interface ReturnTypeInvoiceMapper {
    Integer findLastReturnTypeInvoiceNumber(@Param("startOfMonth")LocalDateTime Month, @Param("startOfNextMonth")LocalDateTime nextMonth);
    void insertReturnTypeInvoice(ReturnTypeInvoice returnTypeInvoice);
    UUID getSenderId(@Param("id") UUID id);
    UUID getCustomerId(@Param("id") UUID id);
    void insertReturnTypeToInvoice(Invoice invoice);
}
