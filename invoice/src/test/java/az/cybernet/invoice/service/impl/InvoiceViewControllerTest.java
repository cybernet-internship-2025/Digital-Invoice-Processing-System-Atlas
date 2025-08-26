package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.controller.InvoiceViewController;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.entity.Invoice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceViewController.class)
public class InvoiceViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceViewService invoiceViewService;

    @Test
    void testGetSentInvoicesByTaxId() throws Exception {
        String taxId = "tax-123";
        UUID senderId = UUID.randomUUID();

        List<FilteredInvoiceResp> mockInvoices = List.of(
                FilteredInvoiceResp.builder().senderId(senderId).total(BigDecimal.valueOf(100.0)).build()
        );

        when(invoiceViewService.getSentInvoicesByTaxId(taxId, any())).thenReturn(mockInvoices);

        mockMvc.perform(get("http://localhost:8080/api/invoice/view/sent/{taxId}", taxId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].senderId").value(senderId.toString()))
                .andExpect(jsonPath("$[0].total").value(100.0));
    }

    @Test
    void testGetReceivedInvoicesByTaxId() throws Exception {
        String taxId = "tax-456";
        UUID customerId = UUID.randomUUID();

        List<FilteredInvoiceResp> mockInvoices = List.of(
                FilteredInvoiceResp.builder().customerId(customerId).total(BigDecimal.valueOf(200.0)).build()
        );

        when(invoiceViewService.getReceivedInvoicesByTaxId(taxId, any())).thenReturn(mockInvoices);

        mockMvc.perform(get("/http://localhost:8080/api/invoice/view/received/{taxId}", taxId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(customerId.toString()))
                .andExpect(jsonPath("$[0].total").value(200.0));
    }

    @Test
    void testGetAllDraftsByTaxId() throws Exception {
        String taxId = "tax-789";
        UUID senderId = UUID.randomUUID();

        List<Invoice> mockInvoices = List.of(
                Invoice.builder().id(UUID.randomUUID()).senderId(senderId).total(BigDecimal.valueOf(50.0)).build()
        );

        when(invoiceViewService.getAllDraftsByTaxId(taxId)).thenReturn(mockInvoices);

        mockMvc.perform(get("http://localhost:8080/api/invoice/view/drafts/{taxId}", taxId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].senderId").value(senderId.toString()))
                .andExpect(jsonPath("$[0].total").value(50.0));
    }

    @Test
    void testGetAllInvoicesByTaxId() throws Exception {
        String taxId = "tax-101";
        UUID senderId = UUID.randomUUID();

        List<Invoice> mockInvoices = List.of(
                Invoice.builder().id(UUID.randomUUID()).senderId(senderId).total(BigDecimal.valueOf(150.0)).build()
        );

        when(invoiceViewService.getAllInvoicesByTaxId(taxId)).thenReturn(mockInvoices);

        mockMvc.perform(get("http://localhost:8080/api/invoice/v1/view/all/{taxId}", taxId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].senderId").value(senderId.toString()))
                .andExpect(jsonPath("$[0].total").value(150.0));
    }
}
