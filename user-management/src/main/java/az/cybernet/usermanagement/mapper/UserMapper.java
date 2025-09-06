package az.cybernet.usermanagement.mapper;

import az.cybernet.usermanagement.entity.Role;
import az.cybernet.usermanagement.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface UserMapper {
    void insertUser(User user);

    Optional<User> findById(@Param("id") UUID id);

    Optional<User> findByUserId(@Param("userId") String userId);

    User findByTaxId(@Param("taxId") String taxId);

    List<User> findAllUsers();

    void updateUser(User user);

    User findByUsername(@Param("name") String name);
    List<Role> getRolesByUserId(@Param("userId") Long userId);

    void updateTaxAndPassword(@Param("id") UUID userId,
                              @Param("taxId") String taxId,
                              @Param("password") String password);

}
