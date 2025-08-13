package az.cybernet.invoice.scheduler;

import az.cybernet.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
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
            lockAtMostFor = "15m"
    )
    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000)
    )
    public void cancelExpiredPendingInvoices() {
        log.info("Starting scheduled job to cancel old pending invoices...");
        invoiceService.cancelExpiredPendingInvoices();
        log.info("Finished scheduled job to cancel old pending invoices successfully.");
    }

    @Recover
    public void recover(Exception e) {
        // This method is called only after all 3 retry attempts have failed.
        log.error("Scheduled job 'cancelExpiredPendingInvoices' failed after all retry attempts.", e);
        log.error("The next execution will be triggered by the scheduler as scheduled.");
    }
}