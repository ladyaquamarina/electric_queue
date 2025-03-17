package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import queue.models.DeputyDeanEntity;

import java.util.UUID;

public interface DeputyDeanRepository extends R2dbcRepository<DeputyDeanEntity, UUID> {
}
