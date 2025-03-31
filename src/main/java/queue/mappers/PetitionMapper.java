package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import queue.dtos.NameValueDto;
import queue.dtos.PetitionDto;
import queue.enums.PetitionPurpose;
import queue.enums.PetitionStatus;
import queue.models.PetitionEntity;

@Mapper(componentModel = "spring",
        uses = {DayScheduleMapper.class,
                DeputyDeanMapper.class,
                StudentMapper.class})
public interface PetitionMapper {
    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "purpose", target = "purpose", qualifiedByName = "purposeDtoToEntity")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusDtoToEntity")
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "deputyDean", ignore = true)
    @Mapping(target = "daySchedule", ignore = true)
    PetitionEntity toEntity(PetitionDto dto);

    @Mapping(source = "purpose", target = "purpose", qualifiedByName = "purposeEntityToDto")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusEntityToDto")
    PetitionDto toDto(PetitionEntity entity);

//    @Named("purposeDtoToEntity")
//    static PetitionPurpose purposeDtoToEntity(NameValueDto dto) {
//        if (dto == null) {
//            return PetitionPurpose.ALL;
//        }
//        return PetitionPurpose.valueOf(dto.getName());
//    }
//
//    @Named("purposeEntityToDto")
//    static NameValueDto purposeEntityToDto(PetitionPurpose entity) {
//        if (entity == null) {
//            return new NameValueDto();
//        }
//        return new NameValueDto(entity.name(), entity.getValue());
//    }
    @Named("statusDtoToEntity")
    static PetitionStatus statusDtoToEntity(NameValueDto dto) {
        if (dto == null) {
            return PetitionStatus.WAITING;
        }
        return PetitionStatus.valueOf(dto.getName());
    }

    @Named("statusEntityToDto")
    static NameValueDto statusEntityToDto(PetitionStatus entity) {
        if (entity == null) {
            return new NameValueDto();
        }
        return new NameValueDto(entity.name(), entity.getValue());
    }
}
