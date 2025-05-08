package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import queue.dtos.StudentDto;
import queue.models.StudentEntity;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class})
public interface StudentMapper {
    @InheritInverseConfiguration
    @Mapping(source = "id", target = "id")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "new", ignore = true)
    StudentEntity toEntity(StudentDto dto);

    @Mapping(source = "id", target = "id")
    StudentDto toDto(StudentEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "group", target = "group", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "course", target = "course", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "faculty", target = "faculty", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "new", ignore = true)
    StudentEntity update(@MappingTarget StudentEntity entity, StudentDto dto);
}
