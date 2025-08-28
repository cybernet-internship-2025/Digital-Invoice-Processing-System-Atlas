package az.cybernet.invoice.controller;


import az.cybernet.invoice.dto.request.CreateReturnTypeRequest;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import az.cybernet.invoice.service.ReturnTypeInvoiceService;
import az.cybernet.invoice.service.impl.ReturnTypeInvoiceServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/invoices/returns")
public class ReturnTypeInvoiceController {
    ReturnTypeInvoiceService returnTypeInvoiceService;
    @PostMapping
    public ResponseEntity<ReturnTypeInvoice> createReturnTypeInvoice(@RequestBody @Valid CreateReturnTypeRequest returnTypeInvoice) {
        return new ResponseEntity<>(returnTypeInvoiceService.createReturnTypeInvoice(returnTypeInvoice), HttpStatus.CREATED);
    }
}
