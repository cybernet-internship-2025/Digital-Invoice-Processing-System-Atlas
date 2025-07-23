package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.mapper.InvoiceMapper;
import az.cybernet.invoice.service.InvoiceNumberGeneratorService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static az.cybernet.invoice.constant.Constants.INVD;

@Service
public class InvoiceNumberGeneratorServiceImpl implements InvoiceNumberGeneratorService {

    private final InvoiceMapper invoiceMapper;

    public InvoiceNumberGeneratorServiceImpl(InvoiceMapper invoiceMapper) {
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public String generateInvoiceNumber() {
        int seq = invoiceMapper.getNextInvoiceNum();
        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());
        String serial = String.format("%04d", seq);
        return INVD + year + month + serial;
    }
}
