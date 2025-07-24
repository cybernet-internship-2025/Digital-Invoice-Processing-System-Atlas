package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper mapper;
    private final InvoiceMapstruct mapstruct;

    public InvoiceServiceImpl(InvoiceMapper mapper, InvoiceMapstruct mapstruct) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
    }

    @Override
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Invoice invoice = mapstruct.toEntity(request);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        mapper.insertInvoice(invoice);
        return mapstruct.toDto(invoice);
    }
}
