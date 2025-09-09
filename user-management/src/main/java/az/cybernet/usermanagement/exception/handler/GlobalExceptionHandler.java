package az.cybernet.usermanagement.exception.handler;

import az.cybernet.usermanagement.exception.*;
import az.cybernet.usermanagement.exception.dto.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ExceptionDto handleFailedToSendSMSException(FailedToSendSMSException e) {
        log.info(e.getMessage());
        return new ExceptionDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleInvalidOtpException(InvalidOtpException e) {
        log.info(e.getMessage());
        return new ExceptionDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handlePhoneNumberNotLinkedException(PhoneNumberNotLinkedException e) {
        log.info(e.getMessage());
        return new ExceptionDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handlePinDataNotFoundException(PinDataNotFoundException e) {
        log.info(e.getMessage());
        return new ExceptionDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleRegistrationNotFoundException(RegistrationNotFoundException e) {
        log.info(e.getMessage());
        return new ExceptionDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleUserNotFoundException(UserNotFoundException e) {
        log.info(e.getMessage());
        return new ExceptionDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleRegistrationAlreadyExistsException(RegistrationAlreadyExistsException e) {
        log.info(e.getMessage());
        return new ExceptionDto(e.getMessage());
    }
}
