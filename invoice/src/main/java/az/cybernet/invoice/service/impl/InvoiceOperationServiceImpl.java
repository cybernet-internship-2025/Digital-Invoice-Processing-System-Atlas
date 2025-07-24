package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.InvoiceOperationRequest;
import az.cybernet.invoice.dto.response.InvoiceOperationResponse;
import az.cybernet.invoice.entity.InvoiceOperation;
import az.cybernet.invoice.mapper.InvoiceOperationMapper;
import az.cybernet.invoice.mapstruct.InvoiceOperationMapstruct;
import az.cybernet.invoice.service.InvoiceOperationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InvoiceOperationServiceImpl implements InvoiceOperationService {

    private final InvoiceOperationMapper mapper;
    private final InvoiceOperationMapstruct mapstruct;

    public InvoiceOperationServiceImpl(InvoiceOperationMapper mapper, InvoiceOperationMapstruct mapstruct) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
    }

    @Override
    public InvoiceOperationResponse insertInvoiceOperation(InvoiceOperationRequest request) {
        InvoiceOperation operation = mapstruct.toEntity(request);
        operation.setTimestamp(LocalDateTime.now());

        mapper.insertInvoiceOperation(operation);
        return mapstruct.toDto(operation);
    }
}
