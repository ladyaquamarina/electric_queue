package queue.services;

import queue.models.AuthenticationInfoEntity;
import queue.models.UserEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthenticationService {

    Mono<AuthenticationInfoEntity> updateChatId(UUID id, Long chatId);

    Mono<UserEntity> createNewUser(String mail);

    Mono<String> checkCode(UUID userId, String code);

    Mono<UUID> checkChatIdMatchUserId(Long chatId, UUID userId);
}
