package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.dto.request.MeasurementRequest;
import az.cybernet.invoice.dto.response.MeasurementResponse;
import az.cybernet.invoice.entity.Measurement;
import az.cybernet.invoice.mapper.MeasurementMapper;
import az.cybernet.invoice.mapstruct.MeasurementMapstruct;
import az.cybernet.invoice.service.MeasurementService;
import org.springframework.stereotype.Service;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementMapper mapper;
    private final MeasurementMapstruct mapstruct;

    public MeasurementServiceImpl(MeasurementMapper mapper, MeasurementMapstruct mapstruct) {
        this.mapper = mapper;
        this.mapstruct = mapstruct;
    }

    @Override
    public MeasurementResponse insertMeasurement(MeasurementRequest request) {
        Measurement measurement = mapstruct.toEntity(request);
        mapper.insertMeasurement(measurement);
        return mapstruct.toDto(measurement);
    }
}
