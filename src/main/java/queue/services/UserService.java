package queue.services;

import queue.dtos.UserDto;
import queue.models.UserEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<UserEntity> createNewUser(UUID userId, UserDto dto);

    Mono<UserEntity> updateUser(UserDto user);
}
