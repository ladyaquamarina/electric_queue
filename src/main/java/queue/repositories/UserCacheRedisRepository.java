package queue.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserCacheRedisRepository {
    private final ReactiveRedisTemplate<String, UUID> redisTemplate;

    private static final String TABLE_NAME = "user_cache";

    public Mono<UUID> findByUserId(UUID userId) {
        return redisTemplate.opsForValue().get(String.format("%s:%s", TABLE_NAME, userId));
    }

    public Mono<UUID> save(UUID upperObjectId, UUID userId) {
        return redisTemplate.opsForValue()
                .set(String.format("%s:%s", TABLE_NAME, userId), upperObjectId)
                .then(findByUserId(userId));
    }

    public Mono<Boolean> delete(UUID userId) {
        return redisTemplate.opsForValue().delete(String.format("%s:%s", TABLE_NAME, userId));
    }
}
