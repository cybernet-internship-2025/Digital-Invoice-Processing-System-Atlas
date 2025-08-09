package az.cybernet.invoice.controller;


import az.cybernet.invoice.dto.request.CreateReturnTypeRequest;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import az.cybernet.invoice.service.impl.ReturnTypeInvoiceServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/invoices")
public class ReturnTypeInvoiceController {
    ReturnTypeInvoiceServiceImpl returnTypeInvoiceService;
    @PostMapping("/return-type/create")
    public ResponseEntity<ReturnTypeInvoice> createReturnTypeInvoice(@RequestBody @Valid CreateReturnTypeRequest returnTypeInvoice) {
        return new ResponseEntity<>(returnTypeInvoiceService.createReturnTypeInvoice(returnTypeInvoice), HttpStatus.CREATED);
    }
}
