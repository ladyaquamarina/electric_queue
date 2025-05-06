package queue.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.enums.PetitionStatus;
import queue.models.PetitionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface PetitionRepository extends R2dbcRepository<PetitionEntity, UUID> {
    // postgres партиции https://habr.com/ru/articles/273933/

    Flux<PetitionEntity> findAllByDayScheduleIdAndStatus(UUID dayScheduleId, PetitionStatus status);

    Mono<PetitionEntity> findByStudentIdAndDayScheduleIdAndStatus(UUID studentId, UUID dayScheduleId, PetitionStatus status);

    Mono<PetitionEntity> findFirstByCreatedAtAndDayScheduleIdAndStatusOrderByCreatedAtAsc(
            LocalDateTime createdAt, UUID dayScheduleId, PetitionStatus status);

    Mono<PetitionEntity> findFirstByDayScheduleIdAndStatusOrderByCreatedAtAsc(UUID dayScheduleId, PetitionStatus status);

    @Query("""
        SELECT *
        FROM petition
        WHERE created_at >= :start
            AND created_at <= :end
            AND (status = 'PROCESSED' OR status = 'NOT_PROCESSED')
        """)
    Flux<PetitionEntity> findAllByCreatedAtPeriod(LocalDate start, LocalDate end);
}
