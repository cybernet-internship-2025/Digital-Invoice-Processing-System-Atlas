package az.cybernet.invoice.util;

import az.cybernet.invoice.mapper.InvoiceMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static az.cybernet.invoice.constant.Constants.INVD;

@Component
public class InvoiceNumberGeneratorUtil {
    private final InvoiceMapper invoiceMapper;

    public InvoiceNumberGeneratorUtil(InvoiceMapper invoiceMapper) {
        this.invoiceMapper = invoiceMapper;
    }

    public String generateInvoiceNumber() {
        int seq = invoiceMapper.getNextInvoiceNum();
        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());
        String serial = String.format("%04d", seq);
        return INVD + year + month + serial;
    }
}
