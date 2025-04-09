package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.enums.PetitionStatus;
import queue.models.PetitionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PetitionRepository extends R2dbcRepository<PetitionEntity, UUID> {
    Flux<PetitionEntity> findAllByDayScheduleIdAndStatus(UUID dayScheduleId, PetitionStatus status);

    Mono<PetitionEntity> findByStudentIdAndDayScheduleIdAndStatus(UUID studentId, UUID dayScheduleId, PetitionStatus status);
}
