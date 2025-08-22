package az.cybernet.usermanagement.service;

import az.cybernet.usermanagement.dto.request.IndividualRegistrationRequest;
import az.cybernet.usermanagement.dto.request.LegalEntityRegistrationRequest;
import az.cybernet.usermanagement.dto.response.IndividualRegistrationResponse;
import az.cybernet.usermanagement.dto.response.LegalEntityRegistrationResponse;

public interface RegistrationService {
    IndividualRegistrationResponse registerIndividual(IndividualRegistrationRequest request);
    LegalEntityRegistrationResponse registerLegalEntity(LegalEntityRegistrationRequest request);
}
