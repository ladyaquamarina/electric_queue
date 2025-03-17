package queue.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import queue.dtos.UserDto;
import queue.models.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @InheritInverseConfiguration
    UserEntity toEntity(UserDto dto);

    UserDto toDto(UserEntity entity);
}
