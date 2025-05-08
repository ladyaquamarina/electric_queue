package queue.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.models.DeputyDeanEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeputyDeanRepository extends R2dbcRepository<DeputyDeanEntity, UUID> {
    Mono<DeputyDeanEntity> findByUserId(UUID userId);

    @Query("""
        SELECT *
        FROM deputy_dean
        WHERE id IN :ids
    """)
    Flux<DeputyDeanEntity> findAllByIds(List<UUID> ids);
}
