package az.cybernet.invoice.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface ReturnTypeInvoiceMapper {
    Integer findLastReturnTypeInvoiceNumber(
            @Param("startOfMonth")LocalDateTime startOfMonth,
            @Param("startOfNextMonth")LocalDateTime startOfNextMonth);
}
