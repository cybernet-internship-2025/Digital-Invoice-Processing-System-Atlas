package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.ProductRequest;
import az.cybernet.invoice.dto.response.ProductResponse;
import az.cybernet.invoice.entity.Product;
import az.cybernet.invoice.mapper.ProductMapper;
import az.cybernet.invoice.mapstruct.ProductMapstruct;
import az.cybernet.invoice.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductMapstruct mapstruct;

    public ProductServiceImpl(ProductMapper mapper, ProductMapstruct mapstruct) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
    }

    @Override
    public ProductResponse insertProduct(ProductRequest request) {
        Product product = mapstruct.toEntity(request);
        mapper.insertProduct(product);
        return mapstruct.toDto(product);
    }
}
