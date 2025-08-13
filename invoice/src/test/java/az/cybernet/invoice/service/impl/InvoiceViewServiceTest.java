package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.client.UserClient;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.mapper.InvoiceViewMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceViewServiceTest {

    private InvoiceViewService invoiceViewService;
    private UserClient userClient;
    private InvoiceViewMapper invoiceViewMapper;

    @BeforeEach
    void setUp() {
        userClient = new UserClient() {
            @Override
            public UserResponse getUserById(UUID id) {
                return new UserResponse(id, "Mock User", "mock-tax");
            }

            @Override
            public UserResponse getUserByTaxId(String taxId) {
                return new UserResponse(UUID.randomUUID(), "Mock User", taxId);
            }
        };
        invoiceViewMapper = mock(InvoiceViewMapper.class);
        invoiceViewService = new InvoiceViewService(userClient, invoiceViewMapper);
    }

    @Test
    void testGetSentInvoicesByTaxId() {
        String taxId = "tax-123";
        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(userId, "Fuad", taxId);
        List<FilteredInvoiceResp> mockInvoices = List.of(FilteredInvoiceResp.builder().senderId(userId).total(100.0).build());

        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);
        when(invoiceViewMapper.getSentInvoicesById(userId, any(), any(), any())).thenReturn(mockInvoices);

        List<FilteredInvoiceResp> result = invoiceViewService.getSentInvoicesByTaxId(taxId, any());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getSenderId());

        verify(userClient).getUserByTaxId(taxId);
        verify(invoiceViewMapper).getSentInvoicesById(userId, any(), any(), any());
    }

    @Test
    void testGetReceivedInvoicesByTaxId() {
        String taxId = "tax-456";
        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(userId, "Fuad", taxId);
        List<FilteredInvoiceResp> mockInvoices = List.of(FilteredInvoiceResp.builder().customerId(userId).total(200.0).build());

        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);
        when(invoiceViewMapper.getReceivedInvoicesById(userId, any(), any(), any())).thenReturn(mockInvoices);

        List<FilteredInvoiceResp> result = invoiceViewService.getReceivedInvoicesByTaxId(taxId, any());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getCustomerId());

        verify(userClient).getUserByTaxId(taxId);
        verify(invoiceViewMapper).getReceivedInvoicesById(userId, any(), any(), any());
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
