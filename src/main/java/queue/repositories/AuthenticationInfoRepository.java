package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.models.AuthenticationInfoEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface AuthenticationInfoRepository extends R2dbcRepository<AuthenticationInfoEntity, UUID> {
    Mono<AuthenticationInfoEntity> findByChatId(Long chatId);
}
