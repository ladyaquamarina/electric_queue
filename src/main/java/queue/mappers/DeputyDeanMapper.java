package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import queue.dtos.DeputyDeanDto;
import queue.models.DeputyDeanEntity;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DeputyDeanMapper {
    @InheritInverseConfiguration
    @Mapping(source = "id", target = "id")
    @Mapping(target = "user", ignore = true)
    DeputyDeanEntity toEntity(DeputyDeanDto dto);

    @Mapping(source = "id", target = "id")
    DeputyDeanDto toDto(DeputyDeanEntity entity);
}
