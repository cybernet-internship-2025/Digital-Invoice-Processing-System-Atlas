package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.CreateReturnTypeRequest;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapper.ReturnTypeInvoiceMapper;
import az.cybernet.invoice.service.ReturnTypeInvoiceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static az.cybernet.invoice.constant.Constants.INR;

@Service
public class ReturnTypeInvoiceServiceImpl implements ReturnTypeInvoiceService {
    ReturnTypeInvoiceMapper returnTypeInvoiceMapper;
    InvoiceMapper invoiceMapper;
    @Override
    public ReturnTypeInvoice createReturnTypeInvoice(CreateReturnTypeRequest returnTypeInvoiceRequest) {
        ReturnTypeInvoice returnType = new ReturnTypeInvoice();
        if(invoiceMapper.findInvoiceById(returnTypeInvoiceRequest.getInitialInvoiceId()).isEmpty()) {
            throw new InvoiceNotFoundException("Invoice not found for ID: " + returnTypeInvoiceRequest.getInitialInvoiceId());
        }
        returnType.setOriginalInvoiceId(returnTypeInvoiceRequest.getInitialInvoiceId());
        returnType.setComment(returnTypeInvoiceRequest.getComment());
        returnType.setProducts(returnTypeInvoiceRequest.getProductQuantityRequests());
        returnType.setId(UUID.randomUUID());
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime Month = dateTime.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime NextMonth = Month.plusMonths(1).withDayOfMonth(1).toLocalDate().atStartOfDay();
        Integer invoiceNumber = returnTypeInvoiceMapper.findLastReturnTypeInvoiceNumber(Month, NextMonth);;
        returnType.setReturnDate(dateTime);
        returnType.setStatus(Status.SENT_TO_RECEIVER);
        returnType.setInvoiceNumber(invoiceNumber);
        returnType.setOriginalInvoiceSeries(returnTypeInvoiceRequest.getInitialInvoiceSeries());

        returnType.setSenderId(returnTypeInvoiceMapper.getSenderId(returnTypeInvoiceRequest.getInitialInvoiceId()));
        returnType.setCustomerId(returnTypeInvoiceMapper.getCustomerId(returnTypeInvoiceRequest.getInitialInvoiceId()));

        if(invoiceNumber == null) {
            invoiceNumber = 1; // If no previous invoice found, start with 1
        } else {
            invoiceNumber++; // Increment the last invoice number
        }
        returnType.setSeries(generateInvoiceSeriesNumber(dateTime) + String.format("%04d", invoiceNumber));

        Invoice invoice = createReturnTypeToInvoice(returnType); // Create an invoice from the return type

        returnTypeInvoiceMapper.insertReturnTypeInvoice(returnType); // Save the return type invoice using mapper
        returnTypeInvoiceMapper.insertReturnTypeToInvoice(invoice); // Save the invoice created from return type

        return returnType; // Return the saved entity
    }
    String generateInvoiceSeriesNumber(LocalDateTime dateTime){
        LocalDateTime dateTime1 = dateTime;
        String year = String.format("%02d", dateTime1.getYear() % 2000);
        String month = String.format("%02d", dateTime1.getMonthValue());
        return INR + year + month;
    }
    Invoice createReturnTypeToInvoice(ReturnTypeInvoice returnType) {
        Invoice invoice = new Invoice();
        invoice.setId(returnType.getId());
        invoice.setSeries(returnType.getSeries());
        invoice.setInvoiceNumber(returnType.getInvoiceNumber());
        invoice.setSenderId(returnType.getSenderId());
        invoice.setCustomerId(returnType.getCustomerId());
        invoice.setStatus(returnType.getStatus());
        invoice.setCreatedAt(returnType.getReturnDate());
        invoice.setComment(returnType.getComment());
        returnTypeInvoiceMapper.insertReturnTypeToInvoice(invoice);
        return invoice;
    }
}
