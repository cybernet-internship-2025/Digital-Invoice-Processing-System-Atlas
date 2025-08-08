package az.cybernet.invoice.util;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.ProductQuantityRequest;
import az.cybernet.invoice.enums.Status;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExcelFileExporter<T> {

    public byte[] createExcelForEntity(List<T> entityList, String[] headers) {
        try(SXSSFWorkbook workbook = new SXSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Report");

            createHeadersRow(sheet, headers);

            int rowNum = 1;
            for (T entity: entityList) {
                Row row = sheet.createRow(rowNum++);
                fillRow(row, entity);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createHeadersRow(Sheet sheet, String[] headers) {
        Row headerRow = sheet.createRow(0);
        for (int i=0; i<headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    private void fillRow(Row row, T entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        int cellNum = 0;

        for (Field field: fields) {
            field.setAccessible(true);
            try {
                var value = field.get(entity);
                Cell cell = row.createCell(cellNum++);

                if (value != null) {
                    if (value instanceof UUID) {
                        cell.setCellValue(value.toString());
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else if (value instanceof LocalDateTime) {
                        cell.setCellValue(value.toString());
                    } else if (value instanceof Status) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                } else {
                    cell.setCellValue("");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ResponseEntity<byte[]> buildExcelResponse(byte[] bytes, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName + ".xlsx").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

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

