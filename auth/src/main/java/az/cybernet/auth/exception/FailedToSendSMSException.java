package az.cybernet.auth.exception;

public class FailedToSendSMSException extends RuntimeException {
    public FailedToSendSMSException(String message) {
        super(message);
    }
}
