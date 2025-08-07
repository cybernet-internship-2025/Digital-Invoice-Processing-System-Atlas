package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.*;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
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
import az.cybernet.invoice.service.InvoiceService;
import az.cybernet.invoice.service.ProductService;
import az.cybernet.invoice.util.InvoicePdfGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static az.cybernet.invoice.constant.Constants.INVD;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper mapper;
    private final InvoiceMapstruct mapstruct;
    private final InvoiceOperationMapper invoiceOperationMapper;

    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;
    private final InvoiceProductMapstruct invoiceProductMapstruct;
    private final ProductMapstruct productMapstruct;
    private final InvoicePdfGenerator pdfGenerator;

    public InvoiceServiceImpl(InvoiceMapper mapper,
                              InvoiceMapstruct mapstruct,
                              InvoiceProductService invoiceProductService,
                              ProductService productService,
                              InvoiceOperationMapper invoiceOperationMapper,
                              InvoiceProductMapstruct invoiceProductMapstruct,
                              ProductMapstruct productMapstruct, InvoicePdfGenerator pdfGenerator) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
        this.invoiceOperationMapper = invoiceOperationMapper;
        this.invoiceProductMapstruct = invoiceProductMapstruct;
        this.productMapstruct = productMapstruct;
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Invoice invoice = mapstruct.toEntity(
                mapstruct.getInvoiceFromCreateRequest(request));
        String invdSeries = generateInvoiceNumber();

        invoice.setId(UUID.randomUUID());
        invoice.setSeries(invdSeries.substring(0, 4));

        //This block exists solely to please GitHub's codescan
        try {
            invoice.setInvoiceNumber(Integer.parseInt(invdSeries.substring(4)));
        } catch (NumberFormatException e) {
            log.warn("Somehow parsing the generated series failed. This is impossible.");
            LocalDateTime datePart = LocalDateTime.now();
            invoice.setInvoiceNumber(datePart.getYear() % 2000 * 1000000
                    + datePart.getMonthValue() * 10000);
            log.info("generated series manually");
        }

        invoice.setStatus(Status.PENDING);
        invoice.setTotal(request.getProductQuantityRequests()
                .stream()
                .map(productQuantityRequest ->
                        productQuantityRequest.getQuantity() * productQuantityRequest.getPrice())
                .reduce(0.0, Double::sum));
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        List<ProductQuantityRequest> productQuantityList = request.getProductQuantityRequests();
        productQuantityList.forEach(productQuantity -> productQuantity.setId(UUID.randomUUID()));


        List<InvoiceProductRequest> invoiceProductList = invoiceProductMapstruct.toInvoiceProductRequestList(
                invoice.getId(),
                productQuantityList);
        invoiceProductList.forEach(invoiceProduct -> invoiceProduct.setActive(true));

        mapper.insertInvoice(invoice);
        productMapstruct.toProductRequestList(productQuantityList).forEach(productService::insertProduct);
        invoiceProductList.forEach(invoiceProductService::insertInvoiceProduct);
        return mapstruct.toDto(invoice);
    }

    @Override
    public InvoiceResponse sendBackForCorrection(UUID id, InvoiceCorrectionReq req) {
        var dateTime = LocalDateTime.now();
        var invoice = Optional.ofNullable(
                mapper.sendBackForCorrection(id, req.getComment(), dateTime)
        ).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found or update failed"));
        var invoiceOperation = mapstruct.invoiceToInvcOper(invoice);
        invoiceOperationMapper.insertInvoiceOperation(invoiceOperation);
        return mapstruct.toDto(invoice);
    }

    @Override
    public String generateInvoiceNumber() {
        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());
        String series = INVD + year + month;

        LocalDateTime startOfMonth = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfNextMonth = now.plusMonths(1).withDayOfMonth(1).atStartOfDay();

        Integer lastNumber = mapper.getLastInvoiceNumberOfMonth(startOfMonth, startOfNextMonth);
        int next = (lastNumber == null) ? 1 : lastNumber + 1;

        return series + String.format("%04d", next);
    }

    @Override
    public InvoiceDetailResponse getInvoiceDetails(UUID invoiceId) {
        return mapper.getDetailedInvoice(invoiceId)
                .map(mapstruct::toDetailDto)
                .orElseThrow(() ->
                        new InvoiceNotFoundException("Invoice not found by id (" + invoiceId + ")"));
    }

    @Override
    @Transactional
    public InvoiceResponse cancelInvoice(UUID id) {
        Invoice cancelledInvoice = mapper.cancelInvoice(id);
        return mapstruct.toDto(cancelledInvoice);
    }

    @Override
    @Transactional
    public InvoiceResponse approveInvoice(UUID id) {
        Invoice invoice = mapper.findInvoiceById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
        if (!invoice.getStatus().equals(Status.PENDING))
            throw new IllegalStateException("Only PENDING invoices can be APPROVED");
        invoice.setUpdatedAt(LocalDateTime.now());
        mapper.approveInvoice(invoice);

        invoiceOperationMapper.insertInvoiceOperation(mapstruct.invoiceToInvcOper(invoice));

        return mapstruct.toDto(invoice);
    }



    public ResponseEntity<byte[]> getInvoicePdf(UUID id) {
        Invoice invoice = mapper.findInvoiceById(id).orElseThrow(
                () -> new InvoiceNotFoundException("Invoice not found"));

        byte[] pdfBytes = InvoicePdfGenerator.generatePdf(invoice);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    @Override
    @Transactional
    public InvoiceResponse updateInvoice(UpdateInvoiceRequest request) {
        Invoice invoice = Optional.ofNullable(mapper.updateInvoice(
                request.getId(),
                request.getStatus(),
                request.getComment(),
                request.getProductQuantityRequests()
                        .stream()
                        .map(productQuantityRequest ->
                                productQuantityRequest.getQuantity() * productQuantityRequest.getPrice())
                        .reduce(0.0, Double::sum),
                LocalDateTime.now())
        ).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));

        InvoiceOperation invoiceOperation = mapstruct.invoiceToInvcOper(invoice);
        invoiceOperationMapper.insertInvoiceOperation(invoiceOperation);

        List<ProductQuantityRequest> productQuantityRequestList = request.getProductQuantityRequests();
        productQuantityRequestList.forEach(productQuantityRequest -> {
            productQuantityRequest.setId(UUID.randomUUID());
            productService.insertProduct(productMapstruct.toProductRequest(productQuantityRequest));
        });

        List<InvoiceProductRequest> invoiceProductRequestList = invoiceProductMapstruct.toInvoiceProductRequestList(
                invoice.getId(),
                productQuantityRequestList
        );

        invoiceProductService.setInactive(invoice.getId());
        invoiceProductRequestList.forEach(invoiceProduct -> invoiceProduct.setActive(true));
        invoiceProductRequestList.forEach(invoiceProductService::insertInvoiceProduct);

        return mapstruct.toDto(invoice);

    }


    public String generateInvoiceHtml(UUID invoiceId) {
        Invoice invoice = mapper.findInvoiceById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        String series = invoice.getSeries() != null ? invoice.getSeries() : "";
        String number = invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber().toString() : "";
        String customerId = invoice.getCustomerId() != null ? invoice.getCustomerId().toString() : "";
        String sellerId = invoice.getId() != null ? invoice.getId().toString() : "";
        String total = invoice.getTotal() != null ? invoice.getTotal().toString() : "";

        return """
                <!DOCTYPE html>
                <html lang="az">
                <head>
                    <meta charset="UTF-8">
                    <title>Elektron Qaimə-Faktura</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            padding: 30px;
                        }
                        table {
                            width: 100%%;
                            border-collapse: collapse;
                        }
                        th, td {
                            border: 1px solid black;
                            padding: 8px;
                            text-align: left;
                            vertical-align: middle;
                        }
                        .center {
                            text-align: center;
                        }
                        .no-border {
                            border: none;
                        }
                        .signature {
                            margin-top: 40px;
                            display: flex;
                            justify-content: space-between;
                        }
                        .signature div {
                            width: 45%%;
                            text-align: center;
                        }
                        .bold {
                            font-weight: bold;
                        }
                        .header {
                            text-align: center;
                            font-size: 18px;
                            font-weight: bold;
                            padding: 12px;
                        }
                        .summary {
                            text-align: right;
                            padding: 10px;
                            font-weight: bold;
                        }
                    </style>
                </head>
                <body>

                <table>
                    <tr>
                        <td colspan="2" class="header">ELEKTRON QAİMƏ-FAKTURA</td>
                    </tr>
                    <tr>
                        <td><b>Tarix:</b></td>
                        <td>%s</td>
                    </tr>
                    <tr>
                        <td><b>SERİYA:</b></td>
                        <td>%s</td>
                    </tr>
                    <tr>
                        <td><b>№:</b></td>
                        <td>%s</td>
                    </tr>
                    <tr>
                        <td><b>TƏQDİM EDƏN VÖEN:</b></td>
                        <td>%s</td>
                    </tr>
                    <tr>
                        <td><b>ALICI VÖEN:</b></td>
                        <td>%s</td>
                    </tr>
                </table>

                <br>

                <table>
                    <tr>
                        <th>No</th>
                        <th>Məhsulun adı</th>
                        <th>Ölçü vahidi</th>
                        <th>Say</th>
                        <th>Məbləğ</th>
                    </tr>
                    <!-- Burada məhsulların siyahısı əlavə edilə bilər -->
                    <tr><td colspan="5" class="center">Məhsul əlavə edilməyib</td></tr>
                </table>

                <div class="summary">
                    Yekun məbləğ: %s
                </div>

                <br>

                <div class="signature">
                    <div>
                        <div class="bold">İCRAÇI: CYBERNET LLC</div>
                        <br><br>
                        İmza ___________________________
                    </div>
                    <div>
                        <div class="bold">MÜŞTƏRİ: </div>
                        <br><br>
                        İmza ___________________________
                    </div>
                </div>

                </body>
                </html>
                """.formatted(LocalDateTime.now(), series,number,sellerId,customerId,total);
    }
}