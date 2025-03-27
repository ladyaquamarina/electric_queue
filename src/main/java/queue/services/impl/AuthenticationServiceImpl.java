package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import queue.models.AuthenticationInfoEntity;
import queue.models.UserEntity;
import queue.repositories.AuthenticationInfoRepository;
import queue.services.AuthenticationService;
import queue.services.UserService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationInfoRepository authenticationInfoRepository;

    private final RedisTemplate<String, Integer> redisTemplate;
    private final UserService userService;

    @Override
    public Mono<AuthenticationInfoEntity> updateChatId(UUID id, Long chatId) {
        return authenticationInfoRepository.save(getEntity(id, chatId));
    }

    @Override
    public Mono<AuthenticationInfoEntity> getByChatId(Long chatId) {
        return authenticationInfoRepository.findByChatId(chatId);
    }

    @Override
    public Mono<UserEntity> createNewUser(String mail) {
        return null;
    }

    @Override
    public Mono<String> checkCode(UUID userId, String code) {
        return null;
    }

    private AuthenticationInfoEntity getEntity(UUID id, Long chatId) {
        return new AuthenticationInfoEntity(id, chatId, false);
    }
}
