package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface PetitionRepository extends R2dbcRepository<PetitionRepository, UUID> {
}
