package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.models.StudentEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface StudentRepository extends R2dbcRepository<StudentEntity, UUID> {
    Mono<StudentEntity> findByUserId(UUID userId);
}
