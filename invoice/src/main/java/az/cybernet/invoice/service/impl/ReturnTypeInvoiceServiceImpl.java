package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.CreateReturnTypeRequest;
import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.request.ProductQuantityRequest;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import az.cybernet.invoice.enums.InvoiceType;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapper.ReturnTypeInvoiceMapper;
import az.cybernet.invoice.mapstruct.InvoiceProductMapstruct;
import az.cybernet.invoice.mapstruct.ProductMapstruct;
import az.cybernet.invoice.service.InvoiceProductService;
import az.cybernet.invoice.service.ProductService;
import az.cybernet.invoice.service.ReturnTypeInvoiceService;
import az.cybernet.invoice.util.InvoiceUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReturnTypeInvoiceServiceImpl implements ReturnTypeInvoiceService {
    private final InvoiceProductMapstruct invoiceProductMapstruct;
    private final ProductMapstruct productMapstruct;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    private final ReturnTypeInvoiceMapper returnTypeInvoiceMapper;
    private final InvoiceMapper invoiceMapper;

    public ReturnTypeInvoiceServiceImpl(InvoiceProductMapstruct invoiceProductMapstruct, ProductMapstruct productMapstruct, InvoiceProductService invoiceProductService, ProductService productService, ReturnTypeInvoiceMapper returnTypeInvoiceMapper, InvoiceMapper invoiceMapper) {
        this.invoiceProductMapstruct = invoiceProductMapstruct;
        this.productMapstruct = productMapstruct;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
        this.returnTypeInvoiceMapper = returnTypeInvoiceMapper;
        this.invoiceMapper = invoiceMapper;
    }


    @Override
    public ReturnTypeInvoice createReturnTypeInvoice(CreateReturnTypeRequest returnTypeInvoiceRequest) {
        Invoice invoice = new Invoice();
        ReturnTypeInvoice returnType = new ReturnTypeInvoice();
        if(invoiceMapper.findInvoiceById(returnTypeInvoiceRequest.getInitialInvoiceId()).isEmpty()) {
            throw new InvoiceNotFoundException("Invoice not found for ID: " + returnTypeInvoiceRequest.getInitialInvoiceId());
        }

        returnType.setOriginalInvoiceId(returnTypeInvoiceRequest.getInitialInvoiceId());
        returnType.setId(UUID.randomUUID());

        invoice.setComment(returnTypeInvoiceRequest.getComment());
        invoice.setId(returnType.getId());
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime Month = dateTime.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime NextMonth = Month.plusMonths(1).withDayOfMonth(1).toLocalDate().atStartOfDay();
        invoice.setCreatedAt(dateTime);
        invoice.setStatus(Status.SENT_TO_RECEIVER);

        invoice.setSenderId(returnTypeInvoiceMapper.getSenderId(returnTypeInvoiceRequest.getInitialInvoiceId()));
        invoice.setCustomerId(returnTypeInvoiceMapper.getCustomerId(returnTypeInvoiceRequest.getInitialInvoiceId()));

        InvoiceUtils invoiceUtils = new InvoiceUtils(invoiceMapper);
        String fullSeries = invoiceUtils.generateSeries(InvoiceType.RETURN);

        invoice.setSeries(fullSeries.substring(0, 3));
        invoice.setInvoiceNumber(Integer.valueOf(fullSeries.substring(3)));

        invoice.setTotal(returnTypeInvoiceRequest.getProductQuantityRequests()
                .stream()
                .map(productQuantityRequest ->
                        productQuantityRequest.getQuantity().multiply(productQuantityRequest.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        invoice.setInvoiceType(InvoiceType.RETURN);

        List<ProductQuantityRequest> productQuantityList = returnTypeInvoiceRequest.getProductQuantityRequests();
        productQuantityList.forEach(productQuantity -> productQuantity.setId(UUID.randomUUID()));


        List<InvoiceProductRequest> invoiceProductList = invoiceProductMapstruct.toInvoiceProductRequestList(
                invoice.getId(),
                productQuantityList);
        invoiceProductList.forEach(invoiceProduct -> invoiceProduct.setActive(true));

        productMapstruct.toProductRequestList(productQuantityList).forEach(productService::insertProduct);
        invoiceProductList.forEach(invoiceProductService::insertInvoiceProduct);

        returnTypeInvoiceMapper.insertReturnTypeInvoice(returnType.getId(), returnType.getOriginalInvoiceId()); // Save the return type invoice using mapper
        returnTypeInvoiceMapper.insertReturnTypeToInvoice(invoice); // Save the invoice created from return type

        return returnType; // Return the saved entity
    }
}
