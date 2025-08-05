package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceOperation;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapper.InvoiceOperationMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static az.cybernet.invoice.constant.Constants.INVD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceMapper mapper;

    @Mock
    private InvoiceMapstruct mapstruct;

    @Mock
    private InvoiceOperationMapper invoiceOperationMapper;

    @InjectMocks
    private InvoiceServiceImpl service;

    @Test
    void sendBackForCorrection_ShouldUpdateInvoice() {
        UUID id = UUID.randomUUID();
        String comment = "Incorrect address";
        InvoiceCorrectionReq req = new InvoiceCorrectionReq();
        req.setComment(comment);

        Invoice invoice = new Invoice();
        invoice.setId(id);

        InvoiceResponse response = new InvoiceResponse();
        InvoiceOperation operation = new InvoiceOperation();

        when(mapper.sendBackForCorrection(eq(id), eq(comment), any())).thenReturn(invoice);
        when(mapstruct.invoiceToInvcOper(invoice)).thenReturn(operation);
        when(mapstruct.toDto(invoice)).thenReturn(response);

        InvoiceResponse result = service.sendBackForCorrection(id, req);

        assertNotNull(result);
        verify(mapper).sendBackForCorrection(eq(id), eq(comment), any());
        verify(invoiceOperationMapper).insertInvoiceOperation(eq(operation));
        verify(mapstruct).toDto(invoice);
    }

    @Test
    void sendBackForCorrection_ShouldThrow_WhenInvoiceNotFound() {
        UUID id = UUID.randomUUID();
        InvoiceCorrectionReq req = new InvoiceCorrectionReq();
        req.setComment("Any comment");

        when(mapper.sendBackForCorrection(eq(id), any(), any())).thenReturn(null);

        assertThrows(InvoiceNotFoundException.class, () -> service.sendBackForCorrection(id, req));
    }

    @Test
    void generateInvoiceNumber_ShouldReturnCorrectFormat_WhenLastNumberIsNull() {
        LocalDate now = LocalDate.now();
        String expectedPrefix = INVD + String.format("%02d", now.getYear() % 100) + String.format("%02d"
                , now.getMonthValue());
        when(mapper.getLastInvoiceNumberOfMonth(any(), any())).thenReturn(null);

        String invoiceNumber = service.generateInvoiceNumber();

        assertTrue(invoiceNumber.startsWith(expectedPrefix));
        assertTrue(invoiceNumber.endsWith("0001"));
    }

    @Test
    void generateInvoiceNumber_ShouldReturnIncrementedNumber_WhenLastNumberExists() {
        LocalDate now = LocalDate.now();
        String expectedPrefix = INVD + String.format("%02d", now.getYear() % 100) + String.format("%02d"
                , now.getMonthValue());
        when(mapper.getLastInvoiceNumberOfMonth(any(), any())).thenReturn(12);

        String invoiceNumber = service.generateInvoiceNumber();

        assertTrue(invoiceNumber.startsWith(expectedPrefix));
        assertTrue(invoiceNumber.endsWith("0013"));
    }

    @Test
    void approveInvoice_shouldApprovePendingInvoiceAndReturnResponse() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setStatus(Status.PENDING);

        InvoiceResponse expectedResponse = new InvoiceResponse();

        when(mapper.findInvoiceById(invoiceId)).thenReturn(Optional.of(invoice));
        when(mapstruct.toDto(invoice)).thenReturn(expectedResponse);
        when(mapstruct.invoiceToInvcOper(invoice)).thenReturn(new InvoiceOperation());

        InvoiceResponse actualResponse = service.approveInvoice(invoiceId);

        assertEquals(expectedResponse, actualResponse);
        verify(mapper).approveInvoice(invoice);
        verify(invoiceOperationMapper).insertInvoiceOperation(any(InvoiceOperation.class));
        verify(mapstruct).toDto(invoice);
    }

    @Test
    void approveInvoice_shouldThrowExceptionIfInvoiceNotFound() {
        UUID invoiceId = UUID.randomUUID();
        when(mapper.findInvoiceById(invoiceId)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> service.approveInvoice(invoiceId));
    }

    @Test
    void approveInvoice_shouldThrowExceptionIfStatusIsNotPending() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setStatus(Status.APPROVED);

        when(mapper.findInvoiceById(invoiceId)).thenReturn(Optional.of(invoice));

        assertThrows(IllegalStateException.class, () -> service.approveInvoice(invoiceId));
    }
}
