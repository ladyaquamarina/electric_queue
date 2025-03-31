package queue.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AuthenticationRedisRepository {
    private final ReactiveRedisTemplate<String, UUID> redisTemplate;

    private static final String TABLE_NAME = "authentication";

    public Mono<UUID> findByChatId(Long chatId) {
        return redisTemplate.opsForValue().get(String.format("%s:%d", TABLE_NAME, chatId));
    }

    public Mono<UUID> save(Long chatId, UUID userId) {
        return redisTemplate.opsForValue()
                .set(String.format("%s:%d", TABLE_NAME, chatId), userId)
                .then(findByChatId(chatId));
    }

    public Mono<Boolean> delete(Long chatId) {
        return redisTemplate.opsForValue().delete(String.format("%s:%d", TABLE_NAME, chatId));
    }
}
