package queue.services;

import queue.dtos.StudentDto;
import queue.models.StudentEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface StudentService {
    Mono<StudentEntity> createNewStudent(UUID userId, StudentDto dto);

    Mono<StudentEntity> updateStudent(Long chatId, StudentDto dto);

    Mono<StudentEntity> getByUserId(UUID userId);
}
