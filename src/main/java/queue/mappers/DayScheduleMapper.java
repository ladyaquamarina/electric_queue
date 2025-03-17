package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import queue.dtos.DayScheduleDto;
import queue.dtos.NameValueDto;
import queue.enums.PetitionPurpose;
import queue.models.DayScheduleEntity;

@Mapper(componentModel = "spring")
public interface DayScheduleMapper {
    @InheritInverseConfiguration
    @Mapping(source = "purpose", target = "purpose", qualifiedByName = "enumDtoToEntity")
    DayScheduleEntity toEntity(DayScheduleDto dto);

    @Mapping(source = "purpose", target = "purpose", qualifiedByName = "enumEntityToDto")
    DayScheduleDto toDto(DayScheduleEntity entity);

    @Named("enumDtoToEntity")
    static PetitionPurpose enumDtoToEntity(NameValueDto dto) {
        if (dto == null) {
            return PetitionPurpose.ALL;
        }
        return PetitionPurpose.valueOf(dto.getName());
    }

    @Named("enumEntityToDto")
    static NameValueDto enumEntityToDto(PetitionPurpose entity) {
        if (entity == null) {
            return new NameValueDto();
        }
        return new NameValueDto(entity.name(), entity.getValue());
    }
}
