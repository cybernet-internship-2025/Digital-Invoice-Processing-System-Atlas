package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.dto.request.IndividualRegistrationRequest;
import az.cybernet.usermanagement.dto.request.LegalEntityRegistrationRequest;
import az.cybernet.usermanagement.dto.response.IndividualRegistrationResponse;
import az.cybernet.usermanagement.dto.response.LegalEntityRegistrationResponse;
import az.cybernet.usermanagement.entity.Registration;
import az.cybernet.usermanagement.enums.RegistrationStatus;
import az.cybernet.usermanagement.enums.RegistrationType;
import az.cybernet.usermanagement.mapper.RegistrationMapper;
import az.cybernet.usermanagement.service.RegistrationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationMapper registrationMapper;

    public RegistrationServiceImpl(RegistrationMapper registrationMapper) {
        this.registrationMapper = registrationMapper;
    }

    @Override
    public IndividualRegistrationResponse registerIndividual(IndividualRegistrationRequest request) {
        Registration registration = new Registration();
        registration.setResidentialAddress(request.getResidentialAddress());
        registration.setPhoneNumber(request.getPhoneNumber());
        registration.setLegalAddress(request.getLegalAddress());
        registration.setRegistrationStatus(RegistrationStatus.WAITING_FOR_APPROVAL);
        registration.setTypeOfRegistration(RegistrationType.INDIVIDUAL);
        registration.setRegistrationNumber(UUID.randomUUID().toString());

        registrationMapper.insertRegistration(registration);

        IndividualRegistrationResponse response = new IndividualRegistrationResponse();
        response.setPhoneNumber(registration.getPhoneNumber());

        return response;
    }

    @Override
    public LegalEntityRegistrationResponse registerLegalEntity(LegalEntityRegistrationRequest request) {
        Registration registration = new Registration();
        registration.setResidentialAddress(request.getResidentialAddress());
        registration.setLegalEntityName(request.getLegalEntityName());
        registration.setPhoneNumber(request.getPhoneNumber());
        registration.setLegalAddress(request.getLegalAddress());
        registration.setRegistrationStatus(RegistrationStatus.WAITING_FOR_APPROVAL);
        registration.setTypeOfRegistration(RegistrationType.LEGAL_ENTITY);
        registration.setRegistrationNumber(UUID.randomUUID().toString());

        registrationMapper.insertRegistration(registration);

        LegalEntityRegistrationResponse response = new LegalEntityRegistrationResponse();
        response.setPhoneNumber(registration.getPhoneNumber());
        response.setLegalEntityName(registration.getLegalEntityName());

        return response;
    }

}
