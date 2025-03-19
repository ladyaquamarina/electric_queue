package queue.services;

import queue.dtos.StudentDto;
import queue.models.StudentEntity;
import reactor.core.publisher.Mono;

public interface StudentService {
    Mono<StudentEntity> createNewStudent(StudentDto dto);

    Mono<StudentEntity> updateStudent(StudentDto dto);
}
