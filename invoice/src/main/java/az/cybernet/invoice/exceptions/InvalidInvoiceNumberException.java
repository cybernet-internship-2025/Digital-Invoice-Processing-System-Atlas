package az.cybernet.invoice.exceptions;

public class InvalidInvoiceNumberException extends RuntimeException {
    public InvalidInvoiceNumberException(String message) {
        super(message);
    }
}
