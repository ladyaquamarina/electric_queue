package queue.services;

import queue.models.AuthenticationInfoEntity;
import queue.models.UserEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthenticationService {
    Mono<UUID> getUserIdByChatId(Long chatId);

    Mono<Void> sendMail(String mail);

    Mono<AuthenticationInfoEntity> updateChatId(UUID id, Long chatId);

    Mono<Long> checkCode(Long chatId, String code);

    Mono<UserEntity> createNewUser(String mail);
}
