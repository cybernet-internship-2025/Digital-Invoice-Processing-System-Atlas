package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.ProductRequest;
import az.cybernet.invoice.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse insertProduct(ProductRequest request);
}
