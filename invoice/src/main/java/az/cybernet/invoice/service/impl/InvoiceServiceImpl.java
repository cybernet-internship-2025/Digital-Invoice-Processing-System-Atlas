package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapper.InvoiceOperationMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.service.InvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static az.cybernet.invoice.constant.Constants.INVD;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper mapper;
    private final InvoiceMapstruct mapstruct;
    private final InvoiceOperationMapper invoiceOperationMapper;

    public InvoiceServiceImpl(InvoiceMapper mapper, InvoiceMapstruct mapstruct, InvoiceOperationMapper invoiceOperationMapper) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
        this.invoiceOperationMapper = invoiceOperationMapper;
    }

    @Override
    public InvoiceResponse insertInvoice(InvoiceRequest request) {
        Invoice invoice = mapstruct.toEntity(request);
        invoice.setId(UUID.randomUUID());
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        mapper.insertInvoice(invoice);
        return mapstruct.toDto(invoice);
    }

    @Override
    public InvoiceResponse sendBackForCorrection(UUID id, InvoiceCorrectionReq req) {
        var dateTime = LocalDateTime.now();
        var invoice = Optional.ofNullable(
                mapper.sendBackForCorrection(id, req.getComment(), dateTime)
        ).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found or update failed"));
        var invoiceOperation = mapstruct.invoiceToInvcOper(invoice);
        invoiceOperationMapper.insertInvoiceOperation(invoiceOperation);
        return mapstruct.toDto(invoice);
    }

    @Override
    public String generateInvoiceNumber() {
        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());
        String series = INVD + year + month;

        LocalDateTime startOfMonth = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfNextMonth = now.plusMonths(1).withDayOfMonth(1).atStartOfDay();

        Integer lastNumber = mapper.getLastInvoiceNumberOfMonth(startOfMonth, startOfNextMonth);
        int next = (lastNumber == null) ? 1 : lastNumber + 1;

        return series + String.format("%04d", next);
    }

    @Override
    public InvoiceDetailResponse getInvoiceDetails(UUID invoiceId) {
        return mapper.getDetailedInvoice(invoiceId)
                .map(mapstruct::toDetailDto)
                .orElseThrow(() ->
                        new InvoiceNotFoundException("Invoice not found by id (" + invoiceId + ")"));
    }

    @Override
    @Transactional
    public InvoiceResponse cancelInvoice(UUID id) {
        Invoice cancelledInvoice = mapper.cancelInvoice(id);
        return mapstruct.toDto(cancelledInvoice);
    }
}