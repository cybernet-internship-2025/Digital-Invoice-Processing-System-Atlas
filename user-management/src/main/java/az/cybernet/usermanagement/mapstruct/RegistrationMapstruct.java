package az.cybernet.usermanagement.mapstruct;

import az.cybernet.usermanagement.dto.request.IndividualRegistrationRequest;
import az.cybernet.usermanagement.dto.request.LegalEntityRegistrationRequest;
import az.cybernet.usermanagement.dto.response.ApproveUserResponse;
import az.cybernet.usermanagement.entity.Registration;
import az.cybernet.usermanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapstruct {
    ApproveUserResponse toApprovalResponse(User user);

    @Mapping(target = "registrationStatus", constant = "WAITING_FOR_APPROVAL")
    @Mapping(target = "typeOfRegistration", constant = "INDIVIDUAL")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "registrationDate", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
    @Mapping(target = "organizationId", source = "organizationId")
    Registration toIndividualRegistration(IndividualRegistrationRequest request);


    @Mapping(target = "registrationStatus", constant = "WAITING_FOR_APPROVAL")
    @Mapping(target = "typeOfRegistration", constant = "LEGAL_ENTITY")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "registrationDate", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
    @Mapping(target = "organizationId", source = "organizationId")
    Registration toLegalEntityRegistration(LegalEntityRegistrationRequest request);

}
