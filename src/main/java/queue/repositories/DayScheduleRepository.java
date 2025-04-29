package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.models.DayScheduleEntity;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface DayScheduleRepository extends R2dbcRepository<DayScheduleEntity, UUID> {
    Flux<DayScheduleEntity> findAllByDeputyDeanId(UUID deputyDeanId);
    Flux<DayScheduleEntity> findAllByDeputyDeanIdAndDateBetween(UUID deputyDeanId, LocalDate start, LocalDate end);
}
