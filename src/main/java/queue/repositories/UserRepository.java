package queue.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.models.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, UUID> {
    @Query("""
        SELECT *
        FROM users
        WHERE id IN :ids
    """)
    Flux<UserEntity> findAllById(List<UUID> ids);
}
