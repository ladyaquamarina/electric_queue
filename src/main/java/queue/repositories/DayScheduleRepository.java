package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import queue.models.DayScheduleEntity;

import java.util.UUID;

public interface DayScheduleRepository extends R2dbcRepository<DayScheduleEntity, UUID> {
}
