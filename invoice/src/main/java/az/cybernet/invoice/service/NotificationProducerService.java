package az.cybernet.invoice.service;



public interface NotificationProducerService {

    void sendInvoiceCreatedNotification(String userId);
}