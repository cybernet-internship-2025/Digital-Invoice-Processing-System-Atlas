package az.cybernet.invoice.service.impl;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.entity.InvoiceDetailed;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceTest {

    @Mock
    private InvoiceMapper mapper;

    @Mock
    private InvoiceMapstruct mapstruct;

    @InjectMocks
    private InvoiceServiceImpl service;

    @Test
    void getInvoiceDetails_shouldReturnDetailResponse_whenInvoiceExists() {

        UUID invoiceId = UUID.randomUUID();
        InvoiceDetailed invoiceDetailed = InvoiceDetailed.builder()
                .id(invoiceId)
                .series("A1")
                .invoiceNumber(1001)
                .senderId(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .status(Status.DRAFT)
                .total(BigDecimal.valueOf(100))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .comment("Test invoice")
                .products(Collections.emptyList())
                .build();

        InvoiceDetailResponse expectedResponse = InvoiceDetailResponse.builder()
                .id(invoiceDetailed.getId())
                .series(invoiceDetailed.getSeries())
                .invoiceNumber(invoiceDetailed.getInvoiceNumber())
                .senderId(invoiceDetailed.getSenderId())
                .customerId(invoiceDetailed.getCustomerId())
                .status(invoiceDetailed.getStatus())
                .total(invoiceDetailed.getTotal())
                .createdAt(invoiceDetailed.getCreatedAt())
                .updatedAt(invoiceDetailed.getUpdatedAt())
                .comment(invoiceDetailed.getComment())
                .products(Collections.emptyList())
                .build();

        when(mapper.getDetailedInvoice(invoiceId)).thenReturn(Optional.of(invoiceDetailed));
        when(mapstruct.toDetailDto(invoiceDetailed)).thenReturn(expectedResponse);

        InvoiceDetailResponse actualResponse = service.getInvoiceDetails(invoiceId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        verify(mapper, times(1)).getDetailedInvoice(invoiceId);
        verify(mapstruct, times(1)).toDetailDto(invoiceDetailed);
    }

    @Test
    void getInvoiceDetails_shouldThrowException_whenInvoiceNotFound() {

        UUID invoiceId = UUID.randomUUID();
        when(mapper.getDetailedInvoice(invoiceId)).thenReturn(Optional.empty());

        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class, () ->
                service.getInvoiceDetails(invoiceId)
        );

        assertEquals("Invoice not found by id (" + invoiceId + ")", exception.getMessage());

        verify(mapper, times(1)).getDetailedInvoice(invoiceId);
        verify(mapstruct, never()).toDetailDto(any());
    }
}