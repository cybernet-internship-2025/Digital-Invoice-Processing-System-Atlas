package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    void insertProduct(Product product);
}
