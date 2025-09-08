package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.MeasurementRequest;
import az.cybernet.invoice.dto.response.MeasurementResponse;
import az.cybernet.invoice.service.MeasurementService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/measurements")
@SecurityRequirement(name = "bearerAuth")
public class MeasurementController {

    private final MeasurementService service;

    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MeasurementResponse> createMeasurement(@RequestBody MeasurementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertMeasurement(request));
    }
}
