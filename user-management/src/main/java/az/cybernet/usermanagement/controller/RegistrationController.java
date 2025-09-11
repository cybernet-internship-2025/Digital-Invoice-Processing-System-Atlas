package az.cybernet.usermanagement.controller;

import az.cybernet.usermanagement.dto.request.IndividualRegistrationRequest;
import az.cybernet.usermanagement.dto.request.LegalEntityRegistrationRequest;
import az.cybernet.usermanagement.dto.response.IndividualRegistrationResponse;
import az.cybernet.usermanagement.dto.response.LegalEntityRegistrationResponse;
import az.cybernet.usermanagement.mapper.RegistrationMapper;
import az.cybernet.usermanagement.service.RegistrationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/registration")
@SecurityRequirement(name = "bearerAuth")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService, RegistrationMapper registrationMapper) {
        this.registrationService = registrationService;
    }

    @PostMapping("/individual")
    public ResponseEntity<IndividualRegistrationResponse> registerIndividual(@RequestBody IndividualRegistrationRequest request) {
        IndividualRegistrationResponse response = registrationService.registerIndividual(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/legal-entity")
    public ResponseEntity<LegalEntityRegistrationResponse> registerLegalEntity(@RequestBody LegalEntityRegistrationRequest request) {
        LegalEntityRegistrationResponse response = registrationService.registerLegalEntity(request);
        return ResponseEntity.ok(response);
    }
}
