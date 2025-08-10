package az.cybernet.invoice.util;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.ProductQuantityRequest;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExcelFileImporter {
    public List<CreateInvoiceRequest> exportCreateRequests(byte[] bytes) {
        try (ByteArrayInputStream input = new ByteArrayInputStream(bytes); Workbook workbook = WorkbookFactory.create(input)) {
            Sheet invoiceSheet = workbook.getSheet("Invoices");
            Sheet productSheet = workbook.getSheet("Products");

            List<CreateInvoiceRequest> createRequests = new ArrayList<>();

            for(Row row : invoiceSheet) {
                int invoiceNumber = (int) row.getCell(0).getNumericCellValue();

                CreateInvoiceRequest createInvoiceRequest = CreateInvoiceRequest.builder()
                        .senderId(UUID.fromString(row.getCell(1).getStringCellValue()))
                        .customerId(UUID.fromString(row.getCell(2).getStringCellValue()))
                        .comment(row.getCell(3).getStringCellValue())
                        .productQuantityRequests(
                                getProductQuantityRequests(productSheet, invoiceNumber)
                        ).build();
            }
            return createRequests;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductQuantityRequest> getProductQuantityRequests(Sheet productSheet, int invoiceNumber) {
        List<ProductQuantityRequest> result = new ArrayList<>();

        for(Row row : productSheet) {
            Cell indexCell = row.getCell(0);

            if(indexCell == null) continue;

            if((int) indexCell.getNumericCellValue() == invoiceNumber) {
                ProductQuantityRequest productQuantityRequest = ProductQuantityRequest.builder()
                        .name(row.getCell(1).getStringCellValue())
                        .price(row.getCell(2).getNumericCellValue())
                        .measurementId(UUID.fromString(row.getCell(3).getStringCellValue()))
                        .quantity(row.getCell(4).getNumericCellValue())
                        .build();
                result.add(productQuantityRequest);
            }
        }

        return result;
    }
}
