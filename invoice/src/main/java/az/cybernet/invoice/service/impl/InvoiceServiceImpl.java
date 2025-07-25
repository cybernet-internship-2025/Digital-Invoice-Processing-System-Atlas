package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.request.ProductRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.service.InvoiceProductService;
import az.cybernet.invoice.service.InvoiceService;
import az.cybernet.invoice.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper mapper;
    private final InvoiceMapstruct mapstruct;

    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    public InvoiceServiceImpl(InvoiceMapper mapper,
                              InvoiceMapstruct mapstruct,
                              InvoiceProductService invoiceProductService,
                              ProductService productService) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Invoice invoice = mapstruct.toEntity(
                mapstruct.getInvoiceFromCreateRequest(request));
        List<ProductRequest> productList = mapstruct.toProductRequestList(request);
        List<InvoiceProductRequest> invoiceProductList = mapstruct.toInvoiceProductRequestList(request);

        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setTotal(request.getProductQuantityRequests()
                .stream()
                .map(productQuantityRequest ->
                    productQuantityRequest.getQuantity() * productQuantityRequest.getPrice())
                .reduce(0.0, Double::sum));

        mapper.insertInvoice(invoice);
        productList.forEach(productService::insertProduct);
        invoiceProductList.forEach(invoiceProductService::insertInvoiceProduct);
        return mapstruct.toDto(invoice);
    }

    @Override
    public InvoiceResponse cancelInvoice(UUID id) {
        Invoice cancelledInvoice = mapper.cancelInvoice(id);
        return mapstruct.toDto(cancelledInvoice);
    }
}
