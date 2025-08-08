package az.cybernet.invoice.scheduler;

import az.cybernet.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceCancellationScheduler {

    private final InvoiceService invoiceService;

    // This will run at 01:00 AM every day
    @Scheduled(cron = "0 0 1 * * ?")
    @SchedulerLock(
            name = "cancelExpiredPendingInvoices",
            lockAtMostFor = "15m",
            lockAtLeastFor = "1m"
    )
    public void cancelExpiredPendingInvoices() {
        log.info("Starting scheduled job to cancel old pending invoices...");
        invoiceService.cancelExpiredPendingInvoices();
        log.info("Finished scheduled job. Releasing lock");
    }
}