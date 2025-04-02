package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.models.AuthenticationInfoEntity;
import queue.models.UserEntity;
import queue.repositories.AuthenticationInfoRepository;
import queue.repositories.AuthenticationRedisRepository;
import queue.services.AuthenticationService;
import queue.services.UserAuthenticationService;
import queue.services.UserService;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationInfoRepository authenticationInfoRepository;
    private final AuthenticationRedisRepository authenticationRedisRepository;
    private final UserAuthenticationService userAuthenticationService;

    @Override
    public Mono<AuthenticationInfoEntity> updateChatId(UUID id, Long chatId) {
        return authenticationInfoRepository.save(getEntity(id, chatId));
    }

    @Override
    public Mono<UserEntity> createNewUser(String mail) {
        return null;
    }

    @Override
    public Mono<String> checkCode(UUID userId, String code) {
        return null;
    }

    @Override
    public Mono<UUID> checkChatIdMatchUserId(Long chatId, UUID userId) {
        return authenticationRedisRepository.findByChatId(chatId)
                .switchIfEmpty(authenticationInfoRepository.findByChatId(chatId)
                        .flatMap(auth -> userAuthenticationService.getByAuthId(auth.getId()))
                        .map(UserEntity::getId))
                .filter(id -> Objects.equals(id, userId));
    }

    private AuthenticationInfoEntity getEntity(UUID id, Long chatId) {
        return new AuthenticationInfoEntity(id, chatId, false);
    }
}
