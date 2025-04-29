package queue.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import queue.models.StartTermInfoEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface StartTermInfoRepository extends R2dbcRepository<StartTermInfoEntity, LocalDate> {
    @Query("""
    SELECT *
    FROM start_term_info
    WHERE start_day <= today
    ORDER BY start_day DESC
    LIMIT 1
    """)
    Mono<StartTermInfoEntity> findFirstByStartDay(LocalDate today);
}
