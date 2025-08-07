package az.cybernet.invoice.util;

import az.cybernet.invoice.enums.Status;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
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
}
