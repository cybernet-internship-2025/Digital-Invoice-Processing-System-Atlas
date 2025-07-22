package az.cybernet.invoice.mapstruct;

import az.cybernet.invoice.dto.request.MeasurementRequest;
import az.cybernet.invoice.dto.response.MeasurementResponse;
import az.cybernet.invoice.entity.Measurement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MeasurementMapstruct {

    Measurement toEntity(MeasurementRequest measurementRequest);

    MeasurementResponse toDto(Measurement measurement);
}
