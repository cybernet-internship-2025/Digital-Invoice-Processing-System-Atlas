package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.response.InvoiceProductResponse;
import az.cybernet.invoice.entity.InvoiceProduct;
import az.cybernet.invoice.mapper.InvoiceProductMapper;
import az.cybernet.invoice.mapstruct.InvoiceProductMapstruct;
import az.cybernet.invoice.service.InvoiceProductService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductMapper mapper;
    private final InvoiceProductMapstruct mapstruct;

    public InvoiceProductServiceImpl(InvoiceProductMapper mapper, InvoiceProductMapstruct mapstruct) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
    }

    @Override
    public InvoiceProductResponse insertInvoiceProduct(InvoiceProductRequest request) {
        InvoiceProduct product = mapstruct.toEntity(request);
        mapper.insertInvoiceProduct(product);
        return mapstruct.toDto(product);
    }

    @Override
    public int setInactive(UUID id) {
        return mapper.setInactive(id);
    }
}
