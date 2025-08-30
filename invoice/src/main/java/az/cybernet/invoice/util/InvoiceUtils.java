package az.cybernet.invoice.util;

import az.cybernet.invoice.enums.InvoiceType;
import az.cybernet.invoice.mapper.InvoiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InvoiceUtils {
    private final InvoiceMapper mapper;

    public InvoiceUtils(InvoiceMapper mapper) {
        this.mapper = mapper;
    }

    public String generateSeries(InvoiceType invoiceType) {
        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());
        String date = year.concat(month);
        LocalDateTime startOfMonth = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfNextMonth = now.plusMonths(1).withDayOfMonth(1).atStartOfDay();
        String start = "INVD";

        if(invoiceType == InvoiceType.STANDARD){
            start = "INVD";
        } else if(invoiceType == InvoiceType.RETURN){
            start = "INR";
        }

        Integer lastNumber = mapper.getLastInvoiceNumberOfMonth(startOfMonth, startOfNextMonth, invoiceType);
        return start + date + String.format("%04d", lastNumber + 1);
    }
}
