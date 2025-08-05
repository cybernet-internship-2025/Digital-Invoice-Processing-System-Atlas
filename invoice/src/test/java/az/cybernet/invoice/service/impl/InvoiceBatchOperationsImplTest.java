package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.IllegalInvoiceException;
import az.cybernet.invoice.mapper.InvoiceBatchOperationsMapper;
import az.cybernet.invoice.service.InvoiceOperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import az.cybernet.invoice.dto.request.InvoiceOperationRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceBatchOperationsImplTest {

    @InjectMocks
    private InvoiceBatchOperationsServiceImpl batchServiceImpl;

    @Mock
    private InvoiceBatchOperationsMapper mapper;

    @Mock
    private InvoiceOperationService operationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Invoice createInvoice(UUID id, Status status) {
        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setStatus(status);
        invoice.setTotal(100.5);
        invoice.setComment("Test comment");
        return invoice;
    }

    @Test
    void testChangeStatusInBatch_EmptyIds_ShouldThrowException() {
        IllegalInvoiceException ex = assertThrows(IllegalInvoiceException.class,
                () -> batchServiceImpl.changeStatusInBatch(Collections.emptyList(), Status.PENDING));
        assertEquals("Batch is empty", ex.getMessage());
    }

    @Test
    void testChangeStatusInBatch_StatusesNotSame_ShouldThrowException() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        List<UUID> ids = List.of(id1, id2);
        List<Invoice> invoices = List.of(
                createInvoice(id1, Status.DRAFT),
                createInvoice(id2, Status.PENDING)
        );

        when(mapper.findAllByIds(ids)).thenReturn(invoices);

        IllegalInvoiceException ex = assertThrows(IllegalInvoiceException.class,
                () -> batchServiceImpl.changeStatusInBatch(ids, Status.CLOSED));
        assertEquals("Not all invoices have the same status", ex.getMessage());
    }

    @Test
    void testChangeStatusInBatch_ValidTransition_DRAFT_To_PENDING() {
        UUID id = UUID.randomUUID();
        List<UUID> ids = List.of(id);
        List<Invoice> invoices = List.of(createInvoice(id, Status.DRAFT));

        when(mapper.findAllByIds(ids)).thenReturn(invoices);

        batchServiceImpl.changeStatusInBatch(ids, Status.PENDING);

        verify(mapper).updateStatusInBatch(eq(ids), eq(Status.PENDING), any(LocalDateTime.class));
        verify(operationService, times(1)).insertInvoiceOperation(any(InvoiceOperationRequest.class));
    }

    @Test
    void testChangeStatusInBatch_InvalidTransition_DRAFT_To_APPROVED_ShouldThrow() {
        UUID id = UUID.randomUUID();
        List<UUID> ids = List.of(id);
        List<Invoice> invoices = List.of(createInvoice(id, Status.DRAFT));

        when(mapper.findAllByIds(ids)).thenReturn(invoices);

        IllegalInvoiceException ex = assertThrows(IllegalInvoiceException.class,
                () -> batchServiceImpl.changeStatusInBatch(ids, Status.APPROVED));
        assertTrue(ex.getMessage().contains("DRAFT status can not be changed"));
    }

    @Test
    void testChangeStatusInBatch_ValidTransition_PENDING_To_APPROVED() {
        UUID id = UUID.randomUUID();
        List<UUID> ids = List.of(id);
        List<Invoice> invoices = List.of(createInvoice(id, Status.PENDING));

        when(mapper.findAllByIds(ids)).thenReturn(invoices);

        batchServiceImpl.changeStatusInBatch(ids, Status.APPROVED);

        verify(mapper).updateStatusInBatch(eq(ids), eq(Status.APPROVED), any(LocalDateTime.class));
        verify(operationService).insertInvoiceOperation(any(InvoiceOperationRequest.class));
    }

    @Test
    void testChangeStatusInBatch_InvalidTransition_PENDING_To_CLOSED_ShouldThrow() {
        UUID id = UUID.randomUUID();
        List<UUID> ids = List.of(id);
        List<Invoice> invoices = List.of(createInvoice(id, Status.PENDING));

        when(mapper.findAllByIds(ids)).thenReturn(invoices);

        IllegalInvoiceException ex = assertThrows(IllegalInvoiceException.class,
                () -> batchServiceImpl.changeStatusInBatch(ids, Status.CLOSED));
        assertTrue(ex.getMessage().contains("PENDING status can not be changed"));
    }
}

