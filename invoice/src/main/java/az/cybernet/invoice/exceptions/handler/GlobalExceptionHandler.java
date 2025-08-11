package az.cybernet.invoice.exceptions.handler;

import az.cybernet.invoice.exceptions.IllegalInvoiceException;
import az.cybernet.invoice.exceptions.InvoiceNotFoundException;
import az.cybernet.invoice.exceptions.dto.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error");

        log.info("Validation error: " + errorMessage);
        return new ExceptionDto(errorMessage);
    }
}
