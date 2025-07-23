package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.MeasurementRequest;
import az.cybernet.invoice.dto.response.MeasurementResponse;
import az.cybernet.invoice.service.MeasurementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/measurements")
public class MeasurementController {

    private final MeasurementService service;

    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MeasurementResponse> insertMeasurement(@RequestBody MeasurementRequest request) {
        return ResponseEntity.ok(service.insertMeasurement(request));
    }
}
