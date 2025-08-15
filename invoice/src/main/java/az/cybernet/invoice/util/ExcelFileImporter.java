package az.cybernet.invoice.util;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.ProductQuantityRequest;
import az.cybernet.invoice.exceptions.ExcelFileParseException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExcelFileImporter {

    public List<CreateInvoiceRequest> getCreateRequests(byte[] bytes) {
        try (ByteArrayInputStream input = new ByteArrayInputStream(bytes); Workbook workbook = WorkbookFactory.create(input)) {
            Sheet invoiceSheet = workbook.getSheetAt(0);
            Sheet productSheet = workbook.getSheetAt(1);

            List<CreateInvoiceRequest> createRequests = new ArrayList<>();

            for(Row row : invoiceSheet) {
                if(row.getCell(0).getCellType() != CellType.NUMERIC) continue;

                int invoiceNumber = (int) row.getCell(0).getNumericCellValue();

                CreateInvoiceRequest createInvoiceRequest = CreateInvoiceRequest.builder()
                        .senderId(UUID.fromString(row.getCell(1).getStringCellValue()))
                        .customerId(UUID.fromString(row.getCell(2).getStringCellValue()))
                        .comment(row.getCell(3).getStringCellValue())
                        .productQuantityRequests(
                                getProductQuantityRequests(productSheet, invoiceNumber)
                        ).build();

                createRequests.add(createInvoiceRequest);
            }
            return createRequests;
        } catch (IOException e) {
            throw new ExcelFileParseException(e.getMessage());
        }
    }

    public List<ProductQuantityRequest> getProductQuantityRequests(Sheet productSheet, int invoiceNumber) {
        List<ProductQuantityRequest> result = new ArrayList<>();

        for(Row row : productSheet) {
            Cell indexCell = row.getCell(0);

            if(indexCell.getCellType() != CellType.NUMERIC) continue;

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
