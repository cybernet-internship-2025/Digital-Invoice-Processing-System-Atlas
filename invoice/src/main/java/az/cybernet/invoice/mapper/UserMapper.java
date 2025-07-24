package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void insertUser(User user);
}
