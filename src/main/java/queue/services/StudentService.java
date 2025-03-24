package queue.services;

import queue.dtos.StudentDto;
import queue.models.StudentEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface StudentService {
    Mono<StudentEntity> createNewStudent(StudentDto dto);

    Mono<StudentEntity> updateStudent(UUID studentId, StudentDto dto);
}
