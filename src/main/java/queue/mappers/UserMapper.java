package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import queue.dtos.UserDto;
import queue.models.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @InheritInverseConfiguration
    @Mapping(source = "id", target = "id")
    UserEntity toEntity(UserDto dto);

    @Mapping(source = "id", target = "id")
    UserDto toDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "firstName", target = "firstName", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "lastName", target = "lastName", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "surName", target = "surName", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "mail", target = "mail", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "new", ignore = true)
    UserEntity update(@MappingTarget UserEntity entity, UserDto dto);
}
