package az.cybernet.invoice.exceptions.handler;

import az.cybernet.invoice.exceptions.IllegalInvoiceException;
import az.cybernet.invoice.exceptions.InvalidInvoiceNumberException;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.exceptions.dto.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleIllegalInvoiceException(IllegalInvoiceException e) {
        log.info(e.getMessage());
        return new ExceptionDto(e.getMessage());
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleInvoiceNotFound(InvoiceNotFoundException e) {
        log.info(e.getMessage());
        return new ExceptionDto(e.getMessage());
    }

    @ExceptionHandler(InvalidInvoiceNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleInvalidInvoiceNumber(InvalidInvoiceNumberException e) {
        log.info(e.getMessage());
        return new ExceptionDto(e.getMessage());
    }
}
