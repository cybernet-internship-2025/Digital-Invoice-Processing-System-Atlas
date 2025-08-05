package az.cybernet.invoice.util;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import az.cybernet.invoice.entity.Invoice;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
@Component
public class InvoicePdfGenerator {

    public static byte[] generatePdf(Invoice invoice) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Invoice"));
        document.add(new Paragraph("Invoice ID: " + invoice.getId()));
        document.add(new Paragraph("Series: " + invoice.getSeries()));
        document.add(new Paragraph("Invoice Number: " + invoice.getInvoiceNumber()));
        document.add(new Paragraph("Sender ID: " + invoice.getSenderId()));
        document.add(new Paragraph("Customer ID: " + invoice.getCustomerId()));
        document.add(new Paragraph("Status: " + invoice.getStatus()));
        document.add(new Paragraph("Total: " + invoice.getTotal()));

        document.close();

        return outputStream.toByteArray(); 
    }
}
