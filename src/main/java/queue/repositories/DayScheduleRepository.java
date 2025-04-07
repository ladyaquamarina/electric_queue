package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.models.DayScheduleEntity;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface DayScheduleRepository extends R2dbcRepository<DayScheduleEntity, UUID> {
    Flux<DayScheduleEntity> findAllByDeputyDeanId(UUID deputyDeanId);
}
