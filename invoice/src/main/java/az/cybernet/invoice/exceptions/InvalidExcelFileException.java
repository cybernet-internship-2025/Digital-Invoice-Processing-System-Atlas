package az.cybernet.invoice.exceptions;

public class InvalidExcelFileException extends RuntimeException {
    public InvalidExcelFileException(String message) {
        super(message);
    }
}
