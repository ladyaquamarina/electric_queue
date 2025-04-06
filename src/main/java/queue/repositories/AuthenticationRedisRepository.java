package queue.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import queue.models.AuthenticationInfoEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AuthenticationRedisRepository {
    private final ReactiveRedisTemplate<String, AuthenticationInfoEntity> redisTemplate;

    private static final String TABLE_NAME = "authentication";

    public Mono<AuthenticationInfoEntity> findByChatId(Long chatId) {
        return redisTemplate.opsForValue().get(String.format("%s:%d", TABLE_NAME, chatId));
    }

    public Mono<AuthenticationInfoEntity> save(Long chatId, AuthenticationInfoEntity authenticationInfo) {
        return redisTemplate.opsForValue()
                .set(String.format("%s:%d", TABLE_NAME, chatId), authenticationInfo)
                .then(findByChatId(chatId));
    }

    public Mono<Boolean> delete(Long chatId) {
        return redisTemplate.opsForValue().delete(String.format("%s:%d", TABLE_NAME, chatId));
    }
}
