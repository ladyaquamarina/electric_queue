package queue.services;

import queue.dtos.UserDto;
import queue.models.UserEntity;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserEntity> createNewUser(Long chatId, UserDto dto);

    Mono<UserEntity> updateUser(UserDto user);
    Mono<UserEntity> getByChatId(Long chatId);
}
