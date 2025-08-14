package az.cybernet.invoice.util;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;


public class HtmlToPdfConverter {
    public static byte[] generatePdfFromHtml(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(outputStream);

            builder.useFont(
                    () -> HtmlToPdfConverter.class.getResourceAsStream("/fonts/DejaVuSans.ttf"),
                    "DejaVu Sans"
            );

            builder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PDF generasiya zamanı xəta baş verdi");
        }
    }
}
