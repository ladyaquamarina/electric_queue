package queue.services;

import queue.dtos.StudentDto;
import queue.models.PetitionEntity;
import queue.models.StudentEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface StudentService {
    Mono<StudentEntity> createNewStudent(UUID userId, StudentDto dto);

    Mono<StudentEntity> updateStudent(Long chatId, StudentDto dto);

    Mono<StudentEntity> getByUserId(UUID userId);

    Flux<StudentEntity> getByStudentIds(List<UUID> studentId);
}
