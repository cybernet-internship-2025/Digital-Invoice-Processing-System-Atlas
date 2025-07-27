package az.cybernet.usermanagement.mapper;

import az.cybernet.usermanagement.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface UserMapper {
    void insertUser(User user);
    User findById(@Param("id") UUID id);
    User findByTaxId(@Param("taxId") String taxId);
    List<User> findAllUsers();
}
