package az.cybernet.invoice.exceptions;

public class IllegalInvoiceException extends RuntimeException {
    public IllegalInvoiceException(String message) {
        super(message);
    }
}
