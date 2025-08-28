package az.cybernet.usermanagement.mapper;

import az.cybernet.usermanagement.entity.GovernmentTaxOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface GovernmentTaxOrganizationMapper {
    Optional<GovernmentTaxOrganization> findById(@Param("id") UUID id);
}