package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.dtos.StudentDto;
import queue.dtos.UserDto;
import queue.mappers.StudentMapper;
import queue.models.StudentEntity;
import queue.models.UserEntity;
import queue.repositories.StudentRepository;
import queue.services.StudentService;
import queue.services.UserService;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final StudentMapper studentMapper;

    @Override
    public Mono<StudentEntity> createNewStudent(UUID userId, StudentDto dto) {
        return userService.createNewUser(userId, dto.getUser())
                .map(user -> dtoToEntity(dto, user))
                .flatMap(student -> studentRepository.findByUserId(student.getUserId())
                        .switchIfEmpty(studentRepository.save(student)));
    }

    @Override
    public Mono<StudentEntity> updateStudent(Long chatId, StudentDto dto) {
        return userService.updateUser(dto.getUser())
                .then(studentRepository.findById(dto.getId()))
                .flatMap(oldStudent -> updateStudentEntity(dto, oldStudent)
                        .flatMap(studentRepository::save)
                        .switchIfEmpty(Mono.just(oldStudent)));
    }

    @Override
    public Mono<StudentEntity> getByUserId(UUID userId) {
        return studentRepository.findByUserId(userId);
    }

    private Mono<StudentEntity> updateStudentEntity(StudentDto newDto, StudentEntity oldEntity) {
        if (newDto.getCourse() != null || newDto.getGroup() != null) {
            return Mono.just(studentMapper.update(oldEntity, newDto));
        }
        return Mono.empty();
    }

    private StudentEntity dtoToEntity(StudentDto dto, UserEntity user) {
        StudentEntity entity = studentMapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        entity.setUser(user);
        entity.setNew(true);
        return entity;
    }
}
