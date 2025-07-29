package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.request.ProductRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapper.InvoiceOperationMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.service.InvoiceNumberGeneratorService;
import az.cybernet.invoice.service.InvoiceProductService;
import az.cybernet.invoice.service.InvoiceService;
import az.cybernet.invoice.service.ProductService;
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

    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;
    private final InvoiceNumberGeneratorService generator;

    public InvoiceServiceImpl(InvoiceMapper mapper,
                              InvoiceMapstruct mapstruct,
                              InvoiceProductService invoiceProductService,
                              ProductService productService,
                              InvoiceNumberGeneratorService generator) {
    public InvoiceServiceImpl(InvoiceMapper mapper, InvoiceMapstruct mapstruct, InvoiceOperationMapper invoiceOperationMapper) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
        this.generator = generator;
        this.invoiceOperationMapper = invoiceOperationMapper;
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Invoice invoice = mapstruct.toEntity(
                mapstruct.getInvoiceFromCreateRequest(request));
        List<ProductRequest> productList = mapstruct.toProductRequestList(request);
        List<InvoiceProductRequest> invoiceProductList = mapstruct.toInvoiceProductRequestList(request);

        String invdSeries = generator.generateInvoiceNumber();

        invoice.setSeries(invdSeries.substring(0, 6));
        invoice.setInvoiceNumber(Integer.parseInt(invdSeries.substring(5)));
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
    public InvoiceResponse sendBackForCorrection(UUID id, InvoiceCorrectionReq req) {
        var dateTime = LocalDateTime.now();
        var invoice = Optional.ofNullable(
                mapper.sendBackForCorrection(id, req.getComment(), dateTime)
        ).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found or update failed"));
        var invoiceOperation = mapstruct.invoiceToInvcOper(invoice);
        invoiceOperationMapper.insertInvoiceOperation(invoiceOperation);
        return mapstruct.toDto(invoice);
    }
}
