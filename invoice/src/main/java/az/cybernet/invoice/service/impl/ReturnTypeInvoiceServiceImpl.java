package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.CreateReturnTypeRequest;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.mapper.ReturnTypeInvoiceMapper;
import az.cybernet.invoice.service.ReturnTypeInvoiceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static az.cybernet.invoice.constant.Constants.INR;

@Service
public class ReturnTypeInvoiceServiceImpl implements ReturnTypeInvoiceService {
    ReturnTypeInvoiceMapper returnTypeInvoiceMapper;
    @Override
    public ReturnTypeInvoice createReturnTypeInvoice(CreateReturnTypeRequest returnTypeInvoiceRequest) {
        ReturnTypeInvoice returnType = new ReturnTypeInvoice();
        returnType.setSenderId(returnTypeInvoiceRequest.getSenderId());
        returnType.setCustomerId(returnTypeInvoiceRequest.getCustomerId());
        returnType.setOriginalInvoiceId(returnTypeInvoiceRequest.getInitialInvoiceId());
        returnType.setComment(returnTypeInvoiceRequest.getComment());
        returnType.setProducts(returnTypeInvoiceRequest.getProductQuantityRequests());
        returnType.setId(UUID.randomUUID());
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime Month = dateTime.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime NextMonth = Month.plusMonths(1).withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime Year = dateTime.withYear(dateTime.getYear() % 2000);
        Integer invoiceNumber = returnTypeInvoiceMapper.findLastReturnTypeInvoiceNumber(Month, NextMonth);;
        returnType.setReturnDate(dateTime);
        returnType.setStatus(Status.PENDING);
        returnType.setInvoiceNumber(invoiceNumber);
        returnType.setOriginalInvoiceSeries(returnTypeInvoiceRequest.getInitialInvoiceSeries());

        if(invoiceNumber == null) {
            invoiceNumber = 1; // If no previous invoice found, start with 1
        } else {
            invoiceNumber++; // Increment the last invoice number
        }
        returnType.setSeries(generateInvoiceSeriesNumber(dateTime) + String.format("%04d", invoiceNumber));


        // Save the return type invoice to the database (repository save logic would go here)
        // For example: returnTypeRepository.save(returnType);

        return returnType; // Return the saved entity
    }
    String generateInvoiceSeriesNumber(LocalDateTime dateTime){
        LocalDateTime dateTime1 = dateTime;
        String year = String.format("%02d", dateTime1.getYear() % 2000);
        String month = String.format("%02d", dateTime1.getMonthValue());
        return INR + year + month;
    }
}
