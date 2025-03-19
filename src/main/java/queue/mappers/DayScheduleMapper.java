package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import queue.dtos.DayScheduleDto;
import queue.dtos.NameValueDto;
import queue.enums.PetitionPurpose;
import queue.models.DayScheduleEntity;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DayScheduleMapper {
    @InheritInverseConfiguration
    @Mapping(source = "purpose", target = "purpose", qualifiedByName = "purposeDtoToEntity")
    @Mapping(target = "deputyDean", ignore = true)
    DayScheduleEntity toEntity(DayScheduleDto dto);

    @Mapping(source = "purpose", target = "purpose", qualifiedByName = "purposeEntityToDto")
    DayScheduleDto toDto(DayScheduleEntity entity);

    @Named("purposeDtoToEntity")
    static PetitionPurpose purposeDtoToEntity(NameValueDto dto) {
        if (dto == null) {
            return PetitionPurpose.ALL;
        }
        return PetitionPurpose.valueOf(dto.getName());
    }

    @Named("purposeEntityToDto")
    static NameValueDto purposeEntityToDto(PetitionPurpose entity) {
        if (entity == null) {
            return new NameValueDto();
        }
        return new NameValueDto(entity.name(), entity.getValue());
    }
}
