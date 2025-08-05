package az.cybernet.invoice.util;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import az.cybernet.invoice.entity.Invoice;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
@Component
public class InvoicePdfGenerator {

    public static byte[] generatePdf(Invoice invoice) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph title = new Paragraph("INVOICE")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(title);

        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 5}))
                .useAllAvailableWidth();

        addRow(table, "Invoice ID:", invoice.getId());
        addRow(table, "Series:", invoice.getSeries());
        addRow(table, "Invoice Number:", invoice.getInvoiceNumber());
        addRow(table, "Sender ID:", invoice.getSenderId());
        addRow(table, "Customer ID:", invoice.getCustomerId());
        addRow(table, "Status:", invoice.getStatus());
        addRow(table, "Total:", invoice.getTotal());

        document.add(table);
        document.close();

        return outputStream.toByteArray();
    }

    private static void addRow(Table table, String label, Object value) {
        String displayValue = (value != null) ? value.toString() : "null";

        Cell labelCell = new Cell().add(new Paragraph(label).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(ColorConstants.GRAY, 0.5f));

        Cell valueCell = new Cell().add(new Paragraph(displayValue))
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(ColorConstants.GRAY, 0.5f));

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
