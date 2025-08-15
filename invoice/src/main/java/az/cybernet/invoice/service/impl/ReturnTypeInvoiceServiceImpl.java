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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static az.cybernet.invoice.constant.Constants.INR;

@Service
public class ReturnTypeInvoiceServiceImpl implements ReturnTypeInvoiceService {
    private final InvoiceProductMapstruct invoiceProductMapstruct;
    private final ProductMapstruct productMapstruct;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    ReturnTypeInvoiceMapper returnTypeInvoiceMapper;
    InvoiceMapper invoiceMapper;

    public ReturnTypeInvoiceServiceImpl(InvoiceProductMapstruct invoiceProductMapstruct, ProductMapstruct productMapstruct, InvoiceProductService invoiceProductService, ProductService productService) {
        this.invoiceProductMapstruct = invoiceProductMapstruct;
        this.productMapstruct = productMapstruct;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
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
        Integer invoiceNumber = returnTypeInvoiceMapper.findLastReturnTypeInvoiceNumber(Month, NextMonth);;
        invoice.setCreatedAt(dateTime);
        invoice.setStatus(Status.SENT_TO_RECEIVER);

        invoice.setSenderId(returnTypeInvoiceMapper.getSenderId(returnTypeInvoiceRequest.getInitialInvoiceId()));
        invoice.setCustomerId(returnTypeInvoiceMapper.getCustomerId(returnTypeInvoiceRequest.getInitialInvoiceId()));

        if(invoiceNumber == null) {
            invoiceNumber = 1; // If no previous invoice found, start with 1
        } else {
            invoiceNumber++; // Increment the last invoice number
        }
        invoice.setInvoiceNumber(invoiceNumber);

        invoice.setSeries(generateInvoiceSeriesNumber(dateTime) + String.format("%04d", invoiceNumber));

        invoice.setTotal(returnTypeInvoiceRequest.getProductQuantityRequests()
                .stream()
                .map(productQuantityRequest ->
                        productQuantityRequest.getQuantity() * productQuantityRequest.getPrice())
                .reduce(0.0, Double::sum));

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

    String generateInvoiceSeriesNumber(LocalDateTime dateTime){
        LocalDateTime dateTime1 = dateTime;
        String year = String.format("%02d", dateTime1.getYear() % 2000);
        String month = String.format("%02d", dateTime1.getMonthValue());
        return INR + year + month;
    }
}
