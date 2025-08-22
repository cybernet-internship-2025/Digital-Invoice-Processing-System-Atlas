package az.cybernet.usermanagement.mapstruct;

import az.cybernet.usermanagement.dto.request.IndividualRegistrationRequest;
import az.cybernet.usermanagement.dto.request.LegalEntityRegistrationRequest;
import az.cybernet.usermanagement.entity.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapstruct {
    ApproveUserResponse toApprovalResponse(User user);

    @Mapping(target = "registrationStatus", constant = "WAITING_FOR_APPROVAL")
    @Mapping(target = "typeOfRegistration", constant = "INDIVIDUAL")
    @Mapping(target = "registrationNumber", expression = "java(java.util.UUID.randomUUID().toString())")
    Registration toIndividualRegistration(IndividualRegistrationRequest request);


    @Mapping(target = "registrationStatus", constant = "WAITING_FOR_APPROVAL")
    @Mapping(target = "typeOfRegistration", constant = "LEGAL_ENTITY")
    @Mapping(target = "registrationNumber", expression = "java(java.util.UUID.randomUUID().toString())")
    Registration toLegalEntityRegistration(LegalEntityRegistrationRequest request);

}
