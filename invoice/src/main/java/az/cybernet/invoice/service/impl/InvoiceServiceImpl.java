package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.service.InvoiceProductService;
import az.cybernet.invoice.service.InvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper mapper;
    private final InvoiceMapstruct mapstruct;

    private final InvoiceProductService invoiceProductService;

    public InvoiceServiceImpl(InvoiceMapper mapper,
                              InvoiceMapstruct mapstruct,
                              InvoiceProductService invoiceProductService) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Invoice invoice = mapstruct.toEntity(
                mapstruct.getInvoiceFromCreateRequest(request));
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        List<InvoiceProductRequest> invoiceProductList = mapstruct.toInvoiceProductRequestList(request);

        mapper.insertInvoice(invoice);
        invoiceProductList.forEach(invoiceProductService::insertInvoiceProduct);
        return mapstruct.toDto(invoice);
    }
}
