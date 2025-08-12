package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.client.UserClient;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.mapper.InvoiceViewMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceViewServiceTest {
    @Mock
    private UserClient userClient;

    @Mock
    private InvoiceViewMapper invoiceViewMapper;

    @InjectMocks
    private InvoiceViewService invoiceViewService;

    @Test
    void testGetSentInvoicesByTaxId() {
        String taxId = "tax-123";
        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(userId, "Fuad", taxId);
        List<Invoice> mockInvoices = List.of(Invoice.builder().id(UUID.randomUUID()).senderId(userId).total(100.0).build());

        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);
        when(invoiceViewMapper.getSentInvoicesById(userId)).thenReturn(mockInvoices);

        List<Invoice> result = invoiceViewService.getSentInvoicesByTaxId(taxId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getSenderId());

        verify(userClient).getUserByTaxId(taxId);
        verify(invoiceViewMapper).getSentInvoicesById(userId);
    }

    @Test
    void testGetReceivedInvoicesByTaxId() {
        String taxId = "tax-456";
        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(userId, "Fuad", taxId);
        List<Invoice> mockInvoices = List.of(Invoice.builder().id(UUID.randomUUID()).customerId(userId).total(200.0).build());

        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);
        when(invoiceViewMapper.getReceivedInvoicesById(userId)).thenReturn(mockInvoices);

        List<Invoice> result = invoiceViewService.getReceivedInvoicesByTaxId(taxId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getCustomerId());

        verify(userClient).getUserByTaxId(taxId);
        verify(invoiceViewMapper).getReceivedInvoicesById(userId);
    }

    @Test
    void testGetAllDraftsByTaxId() {
        String taxId = "tax-789";
        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(userId, "Fuad", taxId);
        List<Invoice> mockInvoices = List.of(Invoice.builder().id(UUID.randomUUID()).senderId(userId).status(null).total(50.0).build());

        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);
        when(invoiceViewMapper.getAllDraftsById(userId)).thenReturn(mockInvoices);

        List<Invoice> result = invoiceViewService.getAllDraftsByTaxId(taxId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getSenderId());

        verify(userClient).getUserByTaxId(taxId);
        verify(invoiceViewMapper).getAllDraftsById(userId);
    }

    @Test
    void testGetAllInvoicesByTaxId() {
        String taxId = "tax-101";
        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(userId, "Fuad", taxId);
        List<Invoice> mockInvoices = List.of(Invoice.builder().id(UUID.randomUUID()).senderId(userId).total(150.0).build());

        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);
        when(invoiceViewMapper.getAllInvoicesById(userId)).thenReturn(mockInvoices);

        List<Invoice> result = invoiceViewService.getAllInvoicesByTaxId(taxId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getSenderId());

        verify(userClient).getUserByTaxId(taxId);
        verify(invoiceViewMapper).getAllInvoicesById(userId);
    }
}
