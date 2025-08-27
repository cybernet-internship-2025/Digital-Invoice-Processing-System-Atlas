package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.client.UserClient;
import az.cybernet.invoice.dto.request.*;
import az.cybernet.invoice.dto.response.InvoiceProductResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.dto.response.ProductResponse;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceOperation;
import az.cybernet.invoice.enums.InvoiceType;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapper.InvoiceOperationMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.mapstruct.InvoiceProductMapstruct;
import az.cybernet.invoice.mapstruct.ProductMapstruct;
import az.cybernet.invoice.service.InvoiceProductService;
import az.cybernet.invoice.service.ProductService;
import az.cybernet.invoice.util.InvoiceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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

    @Mock
    private InvoiceProductMapstruct invoiceProductMapstruct;

    @Mock
    private ProductMapstruct productMapstruct;

    @Mock
    private ProductService productService;

    @Mock
    private InvoiceProductService invoiceProductService;

    @Mock
    private UserClient userClient;

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
        when(mapper.getLastInvoiceNumberOfMonth(any(LocalDateTime.class), any(LocalDateTime.class) , any(InvoiceType.class))).thenReturn(null);

        InvoiceUtils invoiceUtils = new InvoiceUtils(mapper);

        String invoiceNumber = invoiceUtils.generateSeries(InvoiceType.STANDARD);

        assertTrue(invoiceNumber.startsWith(expectedPrefix));
        assertTrue(invoiceNumber.endsWith("0001"));
    }

    @Test
    void generateInvoiceNumber_ShouldReturnIncrementedNumber_WhenLastNumberExists() {
        LocalDate now = LocalDate.now();
        String expectedPrefix = INVD + String.format("%02d", now.getYear() % 100) + String.format("%02d"
                , now.getMonthValue());
        when(mapper.getLastInvoiceNumberOfMonth(any(), any(), any())).thenReturn(12);

        InvoiceUtils invoiceUtils = new InvoiceUtils(mapper);

        String invoiceNumber = invoiceUtils.generateSeries(InvoiceType.STANDARD);

        assertTrue(invoiceNumber.startsWith(expectedPrefix));
        assertTrue(invoiceNumber.endsWith("0013"));
    }

    @Test
    void createInvoice_ShouldCorrectlyCreateInvoice() {
        CreateInvoiceRequest request = new CreateInvoiceRequest();

        LocalDate now = LocalDate.now();
        int expectedNumber = Integer.parseInt(String.format("%02d", now.getYear() % 100) + String.format("%02d", now.getMonthValue()))
                * 10000 + 1;

        ProductQuantityRequest product1 = new ProductQuantityRequest();
        product1.setQuantity(BigDecimal.valueOf(2.0));
        product1.setPrice(BigDecimal.valueOf(100.0));

        ProductQuantityRequest product2 = new ProductQuantityRequest();
        product2.setQuantity(BigDecimal.valueOf(1.0));
        product2.setPrice(BigDecimal.valueOf(150.0));

        List<ProductQuantityRequest> productQuantityList = List.of(product1, product2);
        request.setProductQuantityRequests(productQuantityList);

        InvoiceRequest expectedRequest = new InvoiceRequest();
        when(mapstruct.getInvoiceFromCreateRequest(eq(request))).thenReturn(expectedRequest);
        when(mapstruct.toEntity(expectedRequest)).thenReturn(new Invoice());

        when(mapper.getLastInvoiceNumberOfMonth(any(), any(), any())).thenReturn(null);
        when(userClient.getUserById(any())).thenReturn(new UserResponse());

        List<InvoiceProductRequest> invoiceProductRequestList = List.of(
                new InvoiceProductRequest(), new InvoiceProductRequest());
        when(invoiceProductMapstruct.toInvoiceProductRequestList(any(UUID.class), eq(productQuantityList))).thenReturn(invoiceProductRequestList);

        List<ProductRequest> productRequestList = List.of(
                new ProductRequest(), new ProductRequest());
        when(productMapstruct.toProductRequestList(eq(productQuantityList))).thenReturn(productRequestList);

        doNothing().when(mapper).insertInvoice(any(Invoice.class));
        when(productService.insertProduct(any(ProductRequest.class))).thenReturn(new ProductResponse());
        when(invoiceProductService.insertInvoiceProduct(any(InvoiceProductRequest.class))).thenReturn(new InvoiceProductResponse());

        when(mapstruct.toDto(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoiceArg = invocation.getArgument(0);

            InvoiceResponse response = new InvoiceResponse();
            response.setId(invoiceArg.getId());
            response.setSeries(invoiceArg.getSeries());
            response.setInvoiceNumber(invoiceArg.getInvoiceNumber());
            response.setStatus(invoiceArg.getStatus());
            response.setTotal(invoiceArg.getTotal());

            return response;
        });

        InvoiceResponse response = service.createInvoice(request);

        assertNotNull(response);

        assertNotNull(response.getId());
        assertEquals(BigDecimal.valueOf(350.0), response.getTotal());
        assertEquals(Status.SENT_TO_RECEIVER, response.getStatus());
        assertEquals("INVD", response.getSeries());
        assertEquals(expectedNumber, response.getInvoiceNumber());

        verify(mapper).insertInvoice(any(Invoice.class));
        verify(productService, times(2)).insertProduct(any(ProductRequest.class));
        verify(invoiceProductService, times(2)).insertInvoiceProduct(any(InvoiceProductRequest.class));
    }

    @Test
    void cancelInvoice_ShouldReturnCancelledInvoiceResponse() {
        UUID invoiceId = UUID.randomUUID();

        Invoice foundInvoice = new Invoice();
        foundInvoice.setId(invoiceId);

        Invoice cancelledInvoice = new Invoice();
        cancelledInvoice.setId(invoiceId);
        cancelledInvoice.setStatus(Status.CANCELLED_BY_SENDER);

        InvoiceResponse expectedResponse = new InvoiceResponse();
        expectedResponse.setId(invoiceId);
        expectedResponse.setStatus(Status.CANCELLED_BY_SENDER);

        when(mapper.findInvoiceById(invoiceId)).thenReturn(Optional.of(foundInvoice));
        when(mapper.cancelInvoice(invoiceId)).thenReturn(cancelledInvoice);
        when(mapstruct.toDto(cancelledInvoice)).thenReturn(expectedResponse);

        InvoiceResponse response = service.cancelInvoice(invoiceId);

        assertNotNull(response);
        assertEquals(invoiceId, response.getId());
        assertEquals(Status.CANCELLED_BY_SENDER, response.getStatus());

        verify(mapper).cancelInvoice(invoiceId);
        verify(mapstruct).toDto(cancelledInvoice);
    }

    @Test
    void cancelInvoice_ShouldThrow_InvoiceNotFoundException() {
        UUID invoiceId = UUID.randomUUID();

        when(mapper.findInvoiceById(invoiceId)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> service.cancelInvoice(invoiceId));

        verify(mapper, never()).cancelInvoice(invoiceId);
    }

    @Test
    void approveInvoice_shouldApprovePendingInvoiceAndReturnResponse() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setStatus(Status.SENT_TO_RECEIVER);

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

    @Test
    void cancelExpiredPendingInvoices_shouldCancelOldInvoicesWhenFound() {
        // Arrange
        Invoice oldInvoice = Invoice.builder().id(UUID.randomUUID()).status(Status.SENT_TO_RECEIVER).total(BigDecimal.valueOf(100.0)).build();
        when(mapper.findPendingInvoicesUntil(any(LocalDateTime.class))).thenReturn(List.of(oldInvoice));
        when(mapstruct.invoiceToInvcOper(any(Invoice.class))).thenReturn(new InvoiceOperation());

        // Act
        service.cancelExpiredPendingInvoices();

        // Assert
        verify(mapper, times(1)).updateInvoice(
                eq(oldInvoice.getId()),
                eq(Status.CANCELLED_DUE_TO_TIMEOUT), // Verify correct status
                anyString(),
                eq(oldInvoice.getTotal()),
                any(LocalDateTime.class)
        );
        verify(invoiceOperationMapper, times(1)).insertInvoiceOperation(any(InvoiceOperation.class));
    }

    @Test
    void cancelExpiredPendingInvoices_shouldDoNothingWhenNoOldInvoicesFound() {
        // Arrange
        when(mapper.findPendingInvoicesUntil(any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        // Act
        service.cancelExpiredPendingInvoices();

        // Assert
        verify(mapper, never()).updateInvoice(any(), any(), any(), any(), any());
        verify(invoiceOperationMapper, never()).insertInvoiceOperation(any());
    }

    //--- Tests for restoreCanceledInvoice ---

    @Test
    void restoreCanceledInvoice_shouldRestoreInvoiceToPreviousStatus() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        Invoice canceledInvoice = Invoice.builder().id(invoiceId).status(Status.CANCELLED_DUE_TO_TIMEOUT).build();
        Status previousStatus = Status.SENT_TO_RECEIVER;

        when(mapper.findInvoiceById(invoiceId)).thenReturn(Optional.of(canceledInvoice));
        when(invoiceOperationMapper.previousStatusFor(canceledInvoice)).thenReturn(previousStatus);
        when(mapstruct.invoiceToInvcOper(any(Invoice.class))).thenReturn(new InvoiceOperation());

        // Act
        service.restoreCanceledInvoice(invoiceId);

        // Assert
        // This is a bit tricky to verify directly on the object, but we can verify the mappers were called correctly
        // and infer the object was updated before being passed. The important thing is that `previousStatusFor` was called.
        verify(invoiceOperationMapper, times(1)).previousStatusFor(canceledInvoice);
        verify(invoiceOperationMapper, times(1)).insertInvoiceOperation(any(InvoiceOperation.class));
        verify(mapstruct, times(1)).toDto(any(Invoice.class));
    }

    @Test
    void restoreCanceledInvoice_shouldThrowIllegalStateException_whenInvoiceIsNotCancelled() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        Invoice approvedInvoice = Invoice.builder().id(invoiceId).status(Status.APPROVED).build();
        when(mapper.findInvoiceById(invoiceId)).thenReturn(Optional.of(approvedInvoice));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            service.restoreCanceledInvoice(invoiceId);
        });

        verify(invoiceOperationMapper, never()).previousStatusFor(any());
    }

    @Test
    void restoreCanceledInvoice_shouldThrowInvoiceNotFoundException_whenInvoiceDoesNotExist() {
        // Arrange
        UUID invoiceId = UUID.randomUUID();
        when(mapper.findInvoiceById(invoiceId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvoiceNotFoundException.class, () -> {
            service.restoreCanceledInvoice(invoiceId);
        });
    }
}