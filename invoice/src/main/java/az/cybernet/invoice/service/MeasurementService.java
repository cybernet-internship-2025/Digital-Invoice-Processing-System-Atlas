package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.MeasurementRequest;
import az.cybernet.invoice.dto.response.MeasurementResponse;

public interface MeasurementService {

    MeasurementResponse insertMeasurement(MeasurementRequest request);
}
