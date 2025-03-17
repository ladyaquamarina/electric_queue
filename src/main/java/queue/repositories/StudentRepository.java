package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import queue.models.StudentEntity;

import java.util.UUID;

public interface StudentRepository extends R2dbcRepository<StudentEntity, UUID> {
}
