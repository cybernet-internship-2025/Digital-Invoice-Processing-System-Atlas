package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.client.UserClient;
import az.cybernet.invoice.constant.InvoiceExportHeaders;
import az.cybernet.invoice.dto.request.InvoiceFilterRequest;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.mapper.InvoiceViewMapper;
import az.cybernet.invoice.util.ExcelFileExporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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

    @Mock
    private ExcelFileExporter excelFileExporter;

    @Test
    void testGetSentInvoicesByTaxId() {
        String taxId = "1000000000";
        InvoiceFilterRequest filterRequest = new InvoiceFilterRequest();
        filterRequest.setFullInvoiceNumber("INVD25080001");

        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(userId, "Fuad", taxId);
        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);

        List<FilteredInvoiceResp> expected = List.of(
                FilteredInvoiceResp.builder().fullInvoiceNumber("INVD25080001")
                        .senderId(userId).build()
        );

        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);
        when(invoiceViewMapper.getSentInvoicesById(userId, filterRequest, "INVD", 25080001))
                .thenReturn(expected);

        List<FilteredInvoiceResp> result = invoiceViewService.getSentInvoicesByTaxId(taxId, filterRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("INVD25080001", result.getFirst().getFullInvoiceNumber());
        assertEquals(userId, result.getFirst().getSenderId());

        verify(userClient).getUserByTaxId(taxId);
        verify(invoiceViewMapper).getSentInvoicesById(userId, filterRequest, "INVD", 25080001);
    }

    @Test
    void testGetReceivedInvoicesByTaxId() {
        String taxId = "1000000000";
        InvoiceFilterRequest filterRequest = new InvoiceFilterRequest();
        filterRequest.setFullInvoiceNumber("INVD25080001");

        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(userId, "Fuad", taxId);
        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);

        List<FilteredInvoiceResp> expected = List.of(
                FilteredInvoiceResp.builder().fullInvoiceNumber("INVD25080001")
                        .customerId(userId).build()
        );

        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);
        when(invoiceViewMapper.getSentInvoicesById(userId, filterRequest, "INVD", 25080001))
                .thenReturn(expected);

        List<FilteredInvoiceResp> result = invoiceViewService.getSentInvoicesByTaxId(taxId, filterRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("INVD25080001", result.getFirst().getFullInvoiceNumber());
        assertEquals(userId, result.getFirst().getCustomerId());

        verify(userClient).getUserByTaxId(taxId);
        verify(invoiceViewMapper).getSentInvoicesById(userId, filterRequest, "INVD", 25080001);
    }

    @Test
    void testGetAllDraftsByTaxId() {
        String taxId = "tax-789";
        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(userId, "Fuad", taxId);
        List<Invoice> mockInvoices = List.of(Invoice.builder().id(UUID.randomUUID()).senderId(userId).status(null).total(BigDecimal.valueOf(50.0)).build());

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
        List<Invoice> mockInvoices = List.of(Invoice.builder().id(UUID.randomUUID()).senderId(userId).total(BigDecimal.valueOf(150.0)).build());

        when(userClient.getUserByTaxId(taxId)).thenReturn(userResponse);
        when(invoiceViewMapper.getAllInvoicesById(userId)).thenReturn(mockInvoices);

        List<Invoice> result = invoiceViewService.getAllInvoicesByTaxId(taxId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getSenderId());

        verify(userClient).getUserByTaxId(taxId);
        verify(invoiceViewMapper).getAllInvoicesById(userId);
    }

    @Test
    void testExportSentInvoice() {
        String taxId = "1000000000";
        InvoiceFilterRequest filterRequest = new InvoiceFilterRequest();
        filterRequest.setFullInvoiceNumber("INVD25080001");

        UUID userId = UUID.randomUUID();

        when(userClient.getUserByTaxId(taxId))
                .thenReturn(new UserResponse(userId, "Test Sent User", taxId));

        byte[] expectedBytes = new byte[]{1, 2, 3};
        when(excelFileExporter.createExcelForEntity(anyList(), eq(InvoiceExportHeaders.HEADERS)))
                .thenReturn(expectedBytes);

        byte[] result = invoiceViewService.exportSentInvoice(taxId, filterRequest);

        assertArrayEquals(expectedBytes, result);
        verify(userClient).getUserByTaxId(taxId);
        verify(excelFileExporter).createExcelForEntity(anyList(), eq(InvoiceExportHeaders.HEADERS));
    }

    @Test
    void testExportReceivedInvoice() {
        String taxId = "1000000000";
        InvoiceFilterRequest filterRequest = new InvoiceFilterRequest();
        filterRequest.setFullInvoiceNumber("INVD25080001");

        UUID userId = UUID.randomUUID();

        when(userClient.getUserByTaxId(taxId))
                .thenReturn(new UserResponse(userId, "Test Received User", taxId));

        byte[] expectedBytes = new byte[]{1, 2, 3};
        when(excelFileExporter.createExcelForEntity(anyList(), eq(InvoiceExportHeaders.HEADERS)))
                .thenReturn(expectedBytes);

        byte[] result = invoiceViewService.exportSentInvoice(taxId, filterRequest);

        assertArrayEquals(expectedBytes, result);
        verify(userClient).getUserByTaxId(taxId);
        verify(excelFileExporter).createExcelForEntity(anyList(), eq(InvoiceExportHeaders.HEADERS));
    }
}
