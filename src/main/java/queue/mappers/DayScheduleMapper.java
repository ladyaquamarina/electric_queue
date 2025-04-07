package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import queue.dtos.DayScheduleDto;
import queue.dtos.NameValueDto;
import queue.enums.PetitionPurpose;
import queue.models.DayScheduleEntity;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DayScheduleMapper {
    @InheritInverseConfiguration
    @Mapping(source = "purpose", target = "purpose", qualifiedByName = "purposeDtoToEntity")
    @Mapping(target = "startAtFact", ignore = true)
    @Mapping(target = "endAtFact", ignore = true)
    @Mapping(target = "numberOfStudentFact", ignore = true)
    @Mapping(target = "deputyDean", ignore = true)
    @Mapping(target = "new", ignore = true)
    DayScheduleEntity toEntity(DayScheduleDto dto);

    @Mapping(source = "purpose", target = "purpose", qualifiedByName = "purposeEntityToDto")
    DayScheduleDto toDto(DayScheduleEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deputyDeanId", ignore = true)
    @Mapping(source = "date", target = "date", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "startAtPlan", target = "startAtPlan", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "endAtPlan", target = "endAtPlan", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "numberOfStudentPlan", target = "numberOfStudentPlan", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "startAtFact", ignore = true)
    @Mapping(target = "endAtFact", ignore = true)
    @Mapping(target = "numberOfStudentFact", ignore = true)
    @Mapping(source = "purpose", target = "purpose", qualifiedByName = "purposeDtoToEntity", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "new", ignore = true)
    @Mapping(target = "deputyDean", ignore = true)
    DayScheduleEntity update(@MappingTarget DayScheduleEntity entity, DayScheduleDto dto);

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
