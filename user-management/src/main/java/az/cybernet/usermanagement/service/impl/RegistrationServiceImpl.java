package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.dto.request.IndividualRegistrationRequest;
import az.cybernet.usermanagement.dto.request.LegalEntityRegistrationRequest;
import az.cybernet.usermanagement.dto.response.IndividualRegistrationResponse;
import az.cybernet.usermanagement.dto.response.LegalEntityRegistrationResponse;
import az.cybernet.usermanagement.entity.Registration;
import az.cybernet.usermanagement.mapper.RegistrationMapper;
import az.cybernet.usermanagement.mapstruct.RegistrationMapstruct;
import az.cybernet.usermanagement.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationMapper registrationMapper;
    private final RegistrationMapstruct mapstruct;

    @Override
    public IndividualRegistrationResponse registerIndividual(IndividualRegistrationRequest request) {
        Registration registration = mapstruct.toIndividualRegistration(request);

        registrationMapper.insertRegistration(registration);

        IndividualRegistrationResponse response = new IndividualRegistrationResponse();
        response.setPhoneNumber(registration.getPhoneNumber());

        return response;
    }

    @Override
    public LegalEntityRegistrationResponse registerLegalEntity(LegalEntityRegistrationRequest request) {
        Registration registration = mapstruct.toLegalEntityRegistration(request);

        registrationMapper.insertRegistration(registration);

        LegalEntityRegistrationResponse response = new LegalEntityRegistrationResponse();
        response.setPhoneNumber(registration.getPhoneNumber());
        response.setLegalEntityName(registration.getLegalEntityName());

        return response;

    }
}
