package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import queue.dtos.StudentDto;
import queue.models.StudentEntity;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @InheritInverseConfiguration
    StudentEntity toEntity(StudentDto dto);

    StudentDto toDto(StudentEntity entity);
}
