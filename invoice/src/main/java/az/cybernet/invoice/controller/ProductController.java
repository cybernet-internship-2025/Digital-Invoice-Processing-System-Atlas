package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.ProductRequest;
import az.cybernet.invoice.dto.response.ProductResponse;
import az.cybernet.invoice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> insertProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(service.insertProduct(request));
    }
}
