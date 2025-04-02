package queue.services;

import queue.models.UserEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserAuthenticationService {
    Mono<UserEntity> getByAuthId(UUID authId);
}
