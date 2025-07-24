package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceDetailed;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper mapper;
    private final InvoiceMapstruct mapstruct;

    public InvoiceServiceImpl(InvoiceMapper mapper, InvoiceMapstruct mapstruct) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
    }

    @Override
    public InvoiceResponse insertInvoice(InvoiceRequest request) {
        Invoice invoice = mapstruct.toEntity(request);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        mapper.insertInvoice(invoice);
        return mapstruct.toDto(invoice);
    }

    @Override
    public InvoiceDetailResponse getInvoiceDetails(UUID invoiceId) {
        InvoiceDetailed invoice;
        invoice = mapper.getDetailedInvoice(invoiceId);
        if (invoice == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found by id (" + invoiceId.toString() + ")");
        }
        return mapstruct.toDetailDto(invoice);
    }
}
