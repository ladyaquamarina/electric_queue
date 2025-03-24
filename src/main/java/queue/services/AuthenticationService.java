package queue.services;

import queue.models.UserEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthenticationService {
    Mono<UserEntity> createNewUser(String mail);

    Mono<String> checkCode(UUID userId, String code);
}
