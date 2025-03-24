package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.models.DeputyDeanEntity;

import java.util.UUID;

@Repository
public interface DeputyDeanRepository extends R2dbcRepository<DeputyDeanEntity, UUID> {
}
