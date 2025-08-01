package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.enums.Status;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper
public interface InvoiceBatchOperationsMapper {
    void updateStatusInBatch(@Param("ids") List<UUID> invoiceIds,
                             @Param("status") Status status,
                             @Param("updatedAt") LocalDateTime updatedAt);


    List<Invoice> findAllByIds(@Param("ids") List<UUID> invoiceIds);
}
