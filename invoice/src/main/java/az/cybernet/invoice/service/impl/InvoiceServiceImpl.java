package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.*;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceOperation;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapper.InvoiceOperationMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.mapstruct.InvoiceProductMapstruct;
import az.cybernet.invoice.mapstruct.ProductMapstruct;
import az.cybernet.invoice.service.InvoiceProductService;
import az.cybernet.invoice.service.InvoiceService;
import az.cybernet.invoice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static az.cybernet.invoice.constant.Constants.INVD;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper mapper;
    private final InvoiceMapstruct mapstruct;
    private final InvoiceOperationMapper invoiceOperationMapper;

    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;
    private final InvoiceProductMapstruct invoiceProductMapstruct;
    private final ProductMapstruct productMapstruct;

    public InvoiceServiceImpl(InvoiceMapper mapper,
                              InvoiceMapstruct mapstruct,
                              InvoiceProductService invoiceProductService,
                              ProductService productService,
                              InvoiceOperationMapper invoiceOperationMapper,
                              InvoiceProductMapstruct invoiceProductMapstruct,
                              ProductMapstruct productMapstruct) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
        this.invoiceOperationMapper = invoiceOperationMapper;
        this.invoiceProductMapstruct = invoiceProductMapstruct;
        this.productMapstruct = productMapstruct;
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Invoice invoice = mapstruct.toEntity(
                mapstruct.getInvoiceFromCreateRequest(request));
        String invdSeries = generateInvoiceNumber();

        invoice.setId(UUID.randomUUID());
        invoice.setSeries(invdSeries.substring(0, 4));

        //This block exists solely to please GitHub's codescan
        try {
            invoice.setInvoiceNumber(Integer.parseInt(invdSeries.substring(4)));
        } catch (NumberFormatException e) {
            log.warn("Somehow parsing the generated series failed. This is impossible.");
            LocalDateTime datePart = LocalDateTime.now();
            invoice.setInvoiceNumber(datePart.getYear()%2000*1000000
                    + datePart.getMonthValue() * 10000);
            log.info("generated series manually");
        }

        invoice.setStatus(Status.PENDING);
        invoice.setTotal(request.getProductQuantityRequests()
                .stream()
                .map(productQuantityRequest ->
                        productQuantityRequest.getQuantity() * productQuantityRequest.getPrice())
                .reduce(0.0, Double::sum));
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        List<ProductRequest> productList = productMapstruct.toProductRequestList(request.getProductQuantityRequests());

        List<InvoiceProductRequest> invoiceProductList = invoiceProductMapstruct.toInvoiceProductRequestList(
                invoice.getId(),
                request.getProductQuantityRequests());
        invoiceProductList.forEach(invoiceProduct -> invoiceProduct.setActive(true));

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



    @Transactional
    public InvoiceResponse cancelInvoice(UUID id) {
        Invoice cancelledInvoice = mapper.cancelInvoice(id);
        return mapstruct.toDto(cancelledInvoice);
    }

    @Override
    @Transactional
    public InvoiceResponse updateInvoice(UpdateInvoiceRequest request) {
        Invoice invoice = Optional.ofNullable(mapper.updateInvoice(
                request.getId(),
                request.getStatus(),
                request.getComment(),
                request.getProductQuantityRequests()
                        .stream()
                        .map(productQuantityRequest ->
                                productQuantityRequest.getQuantity() * productQuantityRequest.getPrice())
                        .reduce(0.0, Double::sum),
                LocalDateTime.now())
        ).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));

        InvoiceOperation invoiceOperation = mapstruct.invoiceToInvcOper(invoice);
        invoiceOperationMapper.insertInvoiceOperation(invoiceOperation);

        List<ProductRequest> productRequestList = productMapstruct.toProductRequestList(request.getProductQuantityRequests());
        productRequestList.forEach(productService::insertProduct);

        List<InvoiceProductRequest> invoiceProductRequestList = invoiceProductMapstruct.toInvoiceProductRequestList(
                invoice.getId(),
                request.getProductQuantityRequests()
        );
        invoiceProductService.setInactive(invoice.getId());
        invoiceProductRequestList.forEach(invoiceProduct -> invoiceProduct.setActive(true));
        invoiceProductRequestList.forEach(invoiceProductService::insertInvoiceProduct);

        return mapstruct.toDto(invoice);
    }
}