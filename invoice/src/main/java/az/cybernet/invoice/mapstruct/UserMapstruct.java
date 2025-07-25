package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.UserRequest;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapstruct {

    User toEntity(UserRequest userRequest);

    UserResponse toDto(User user);

    List<Invoice> getByTaxId(User user);

    List<Invoice> getSentInvoicesByTaxId(@Param ("taxId") String taxId);
    List<Invoice> getReceivedInvoicesByTaxId(@Param("taxId") String taxId);
    List<Invoice> getAllDraftsByTaxId(@Param("taxId") String taxId);
    List<Invoice> getAllInvoicesByTaxId(@Param("taxId") String taxId);
}
