package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import queue.dtos.PetitionDto;
import queue.models.PetitionEntity;

@Mapper(componentModel = "spring")
public interface PetitionMapper {
    @InheritInverseConfiguration
    PetitionEntity toEntity(PetitionDto dto);

    PetitionDto toDto(PetitionEntity entity);
}
