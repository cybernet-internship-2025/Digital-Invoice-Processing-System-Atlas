package az.cybernet.usermanagement.mapper;

import az.cybernet.usermanagement.entity.GovernmentTaxOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface TaxOrganizationMapper {
    Optional<GovernmentTaxOrganization> findOrganizationById(@Param("id") UUID id);
}
