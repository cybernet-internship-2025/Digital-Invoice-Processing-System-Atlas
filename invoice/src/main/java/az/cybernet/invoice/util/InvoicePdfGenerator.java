package az.cybernet.invoice.util;

import az.cybernet.invoice.entity.Invoice;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
@Component
public class InvoicePdfGenerator {

    public static byte[] generatePdf(Invoice invoice) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont("invoice/src/main/resources/fonts/DejaVuSans.ttf", PdfEncodings.IDENTITY_H);

            Paragraph title = new Paragraph("ELEKTRON QAİMƏ-FAKTURA")
                    .setFont(font)
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 5}))
                    .useAllAvailableWidth();

            addRow(table, "Seriya:", invoice.getSeries(), font);
            addRow(table, "№: ", invoice.getInvoiceNumber(), font);
            addRow(table, "Təqdim edən: VÖEN",invoice.getId(), font);
            addRow(table, "Alıcı: VÖEN", invoice.getCustomerId(), font);
            addRow(table, "Yekun məbləğ:", invoice.getTotal(), font);

            document.add(table);
            document.close();
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("PDF creation error", e);
        }
    }

    private static void addRow(Table table, String label, Object value, PdfFont font) {
        String displayValue = (value != null) ? value.toString() : "null";

        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFont(font).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(ColorConstants.GRAY, 0.5f));

        Cell valueCell = new Cell()
                .add(new Paragraph(displayValue).setFont(font))
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(ColorConstants.GRAY, 0.5f));

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}