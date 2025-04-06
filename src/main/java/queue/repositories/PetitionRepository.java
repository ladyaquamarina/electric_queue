package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.models.PetitionEntity;

import java.util.UUID;

@Repository
public interface PetitionRepository extends R2dbcRepository<PetitionEntity, UUID> {
}
