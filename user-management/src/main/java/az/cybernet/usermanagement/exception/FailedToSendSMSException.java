package az.cybernet.usermanagement.exception;

public class FailedToSendSMSException extends RuntimeException {
    public FailedToSendSMSException(String message) {
        super(message);
    }
}
