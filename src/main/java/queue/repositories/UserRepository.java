package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import queue.models.UserEntity;

import java.util.UUID;

public interface UserRepository extends R2dbcRepository<UserEntity, UUID> {
}
