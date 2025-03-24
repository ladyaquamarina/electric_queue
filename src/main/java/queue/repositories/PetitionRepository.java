package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PetitionRepository extends R2dbcRepository<PetitionRepository, UUID> {
}
