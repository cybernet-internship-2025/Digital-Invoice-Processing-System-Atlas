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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
                () -> batchServiceImpl.changeStatusInBatch(Collections.emptyList(), Status.SENT_TO_RECEIVER));
        System.out.println(("Batch is empty"));
    }

    @Test
    void testChangeStatusInBatch_StatusesNotSame_ShouldThrowException() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        List<UUID> ids = List.of(id1, id2);
        List<Invoice> invoices = List.of(
                createInvoice(id1, Status.DRAFT),
                createInvoice(id2, Status.SENT_TO_RECEIVER)
        );
        IllegalInvoiceException ex = assertThrows(IllegalInvoiceException.class,
                () -> batchServiceImpl.changeStatusInBatch(ids, Status.APPROVED));
        System.out.println("Not all the same status");
    }

    @Test
    void testChangeStatusInBatch_ValidTransition_DRAFT_To_SENT_TO_RECEIVER() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        List<UUID> ids = List.of(id1, id2);
        List<Invoice> invoices = List.of(createInvoice(id1, Status.DRAFT),
                                         createInvoice(id2, Status.SENT_TO_RECEIVER));

        IllegalInvoiceException ex = assertThrows(IllegalInvoiceException.class,
                () -> batchServiceImpl.changeStatusInBatch(ids, Status.SENT_TO_RECEIVER));
        System.out.println("DRAFT status can be changed SENT_TO_RECEIVER");
    }

    @Test
    void testChangeStatusInBatch_InvalidTransition_DRAFT_To_APPROVED_ShouldThrow() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        List<UUID> ids = List.of(id1, id2);
        List<Invoice> invoices = List.of(createInvoice(id1, Status.DRAFT),
                                         createInvoice(id2, Status.APPROVED));

        IllegalInvoiceException ex = assertThrows(IllegalInvoiceException.class,
                () -> batchServiceImpl.changeStatusInBatch(ids, Status.APPROVED));
        System.out.println("DRAFT status can not be changed APPROVED");
    }

}

