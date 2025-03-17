package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import queue.dtos.DeputyDeanDto;
import queue.models.DeputyDeanEntity;

@Mapper(componentModel = "spring")
public interface DeputyDeanMapper {
    @InheritInverseConfiguration
    DeputyDeanEntity toEntity(DeputyDeanDto dto);

    DeputyDeanDto toDto(DeputyDeanEntity entity);
}
