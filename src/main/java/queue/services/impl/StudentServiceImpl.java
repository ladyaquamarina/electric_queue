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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final StudentMapper studentMapper;

    @Override
    public Mono<StudentEntity> createNewStudent(Long chatId, StudentDto dto) {
        return userService.createNewUser(chatId, dto.getUser())
                .map(user -> dtoToEntity(dto, user))
                .flatMap(student -> studentRepository.findByUserId(student.getUserId())
                        .switchIfEmpty(studentRepository.save(student)));
    }

    @Override
    public Mono<StudentEntity> updateStudent(Long chatId, StudentDto dto) {
        return userService.getByChatId(chatId)
    }

    private Mono<StudentEntity> updateStudent(StudentDto dto) {
        return checkNecessityToUpdateUser(dto.getUser())
                .flatMap(check -> userService.updateUser(dto.getUser()))
                .then(studentRepository.findById(dto.getId()))
                .flatMap(oldStudent -> updateStudent(dto, oldStudent)
                        .flatMap(studentRepository::save)
                        .switchIfEmpty(Mono.just(oldStudent)));
    }

    private Mono<Boolean> checkNecessityToUpdateUser(UserDto dto) {
        if (dto.getFirstName() == null && dto.getLastName() == null && dto.getSurName() == null) {
            return Mono.just(false);
        }
        return Mono.empty();
    }

    private Mono<StudentEntity> updateStudent(StudentDto dto, StudentEntity oldEntity) {
        if (dto.getCourse() != null || dto.getGroup() != null) {
            return Mono.just(studentMapper.update(oldEntity, dto));
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
