package queue.services;

import queue.models.AuthenticationInfoEntity;
import queue.models.UserEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthenticationService {
    Mono<UUID> getUserIdByChatId(Long chatId);

    Mono<Void> sendMail(String mail, Long chatId);

    Mono<UUID> checkCode(Long chatId, String code);
}
