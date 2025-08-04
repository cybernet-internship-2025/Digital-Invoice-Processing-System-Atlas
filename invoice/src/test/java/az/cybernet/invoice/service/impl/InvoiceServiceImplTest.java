package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.*;
import az.cybernet.invoice.dto.response.InvoiceProductResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.dto.response.ProductResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceOperation;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.mapper.InvoiceOperationMapper;
import az.cybernet.invoice.mapstruct.InvoiceMapstruct;
import az.cybernet.invoice.mapstruct.InvoiceProductMapstruct;
import az.cybernet.invoice.mapstruct.ProductMapstruct;
import az.cybernet.invoice.service.InvoiceProductService;
import az.cybernet.invoice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        when(mapper.getLastInvoiceNumberOfMonth(any(), any())).thenReturn(null);

        String invoiceNumber = service.generateInvoiceNumber();

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
        product1.setQuantity(2.0);
        product1.setPrice(100.0);

        ProductQuantityRequest product2 = new ProductQuantityRequest();
        product2.setQuantity(1.0);
        product2.setPrice(150.0);

        List<ProductQuantityRequest> productQuantityList = List.of(product1, product2);
        request.setProductQuantityRequests(productQuantityList);

        InvoiceRequest expectedRequest = new InvoiceRequest();
        when(mapstruct.getInvoiceFromCreateRequest(eq(request))).thenReturn(expectedRequest);
        when(mapstruct.toEntity(expectedRequest)).thenReturn(new Invoice());

        when(mapper.getLastInvoiceNumberOfMonth(any(), any())).thenReturn(null);

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
        assertEquals(350.0, response.getTotal());
        assertEquals(Status.PENDING, response.getStatus());
        assertEquals("INVD", response.getSeries());
        assertEquals(expectedNumber, response.getInvoiceNumber());

        verify(mapper).insertInvoice(any(Invoice.class));
        verify(productService, times(2)).insertProduct(any(ProductRequest.class));
        verify(invoiceProductService, times(2)).insertInvoiceProduct(any(InvoiceProductRequest.class));
    }
}
