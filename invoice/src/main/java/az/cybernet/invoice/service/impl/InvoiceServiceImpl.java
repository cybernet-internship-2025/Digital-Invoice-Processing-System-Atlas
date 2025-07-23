package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.service.InvoiceService;
import org.springframework.stereotype.Service;

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

        mapper.insertInvoice(invoice);

        InvoiceResponse response = mapstruct.toDto(invoice);

        return response;
    }

//    @Override
//    public InvoiceResponse insertInvoice(InvoiceRequest request) {
//        Invoice invoice = Invoice.builder()
//                .id(UUID.randomUUID())
//                .series(request.getSeries())
//                .invoiceNumber(request.getInvoiceNumber())
//                .senderId(request.getSenderId())
//                .customerId(request.getCustomerId())
//                .status(request.getStatus())
//                .total(request.getTotal())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .comment(request.getComment())
//                .build();
//
//        mapper.insertInvoice(invoice);
//
//        InvoiceResponse response = InvoiceResponse.builder()
//                .id(request.getId())
//                .series(request.getSeries())
//                .invoiceNumber(request.getInvoiceNumber())
//                .senderId(request.getSenderId())
//                .customerId(request.getCustomerId())
//                .status(request.getStatus())
//                .total(request.getTotal())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .comment(request.getComment())
//                .build();
//
//        return response;
//    }
}
