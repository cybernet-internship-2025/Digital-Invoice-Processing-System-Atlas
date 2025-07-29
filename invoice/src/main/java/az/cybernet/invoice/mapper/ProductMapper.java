package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

@Mapper
public interface ProductMapper {

    void insertProduct(Product product);
    Product findProductById(@Param("id")UUID id);
}
