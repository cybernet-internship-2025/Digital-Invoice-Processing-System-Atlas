package az.cybernet.invoice.api.service;

import az.cybernet.invoice.dto.request.ProductRequest;
import az.cybernet.invoice.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse insertProduct(ProductRequest request);
}
