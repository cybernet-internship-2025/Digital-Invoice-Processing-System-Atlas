package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Measurement;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeasurementMapper {

    void insertMeasurement(Measurement measurement);
}
