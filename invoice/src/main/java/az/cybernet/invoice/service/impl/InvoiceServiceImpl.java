package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.ApprovedInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceOperation;
import az.cybernet.invoice.entity.InvoiceProduct;
import az.cybernet.invoice.entity.Product;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.exceptions.ProductNotFoundException;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapper.InvoiceOperationMapper;
import az.cybernet.invoice.mapper.InvoiceProductMapper;
import az.cybernet.invoice.mapper.ProductMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.service.InvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper mapper;
    private final InvoiceMapstruct mapstruct;
    private final InvoiceOperationMapper invoiceOperationMapper;
    private final InvoiceProductMapper invoiceProductMapper;
    private final ProductMapper productMapper;

    public InvoiceServiceImpl(
            InvoiceMapper mapper,
            InvoiceMapstruct mapstruct,
            InvoiceOperationMapper invoiceOperationMapper,
            InvoiceProductMapper invoiceProductMapper,
            ProductMapper productMapper) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
        this.invoiceOperationMapper = invoiceOperationMapper;
        this.invoiceProductMapper = invoiceProductMapper;
        this.productMapper = productMapper;
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
    @Transactional
    public InvoiceResponse approveInvoice(UUID id, ApprovedInvoiceRequest request) {
        Invoice invoice = mapper.findInvoiceById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
        if (!invoice.getStatus().equals(Status.DRAFT))
            throw new RuntimeException("Only DRAFT invoices can be APPROVED");

        List<InvoiceProduct> productList = invoiceProductMapper.findActiveByInvoiceId(id);
        if (productList.isEmpty())
            throw new IllegalStateException("Invoice must contain at least one active product");

        double total = 0;
        for (InvoiceProduct item: productList) {
            Product product = productMapper.findProductById(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));
            if (product.getMeasurementId() == null)
                throw new IllegalStateException("Product " + product.getName() + " has no measurement.");
            total += product.getPrice() * item.getQuantity();
        }

        invoice.setStatus(Status.APPROVED);
        invoice.setTotal(total);
        invoice.setUpdatedAt(LocalDateTime.now());

        InvoiceOperation operation = mapstruct.invoiceToInvcOper(invoice);
        operation.setComment(request.getComment());
        invoiceOperationMapper.insertInvoiceOperation(operation);

        return mapstruct.toDto(invoice);
  
    }

    @Override
    public InvoiceResponse cancelInvoice(UUID id) {
        Invoice cancelledInvoice = mapper.cancelInvoice(id);
        return mapstruct.toDto(cancelledInvoice);
    }
}
