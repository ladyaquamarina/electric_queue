package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.dtos.PetitionDto;
import queue.enums.PetitionStatus;
import queue.mappers.PetitionMapper;
import queue.models.DeputyDeanEntity;
import queue.models.PetitionEntity;
import queue.models.StudentEntity;
import queue.repositories.PetitionRepository;
import queue.services.DeputyDeanService;
import queue.services.PetitionService;
import queue.services.QueueService;
import queue.services.StudentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetitionServiceImpl implements PetitionService {
    private final PetitionRepository petitionRepository;

    private final StudentService studentService;
    private final DeputyDeanService deputyDeanService;
    private final QueueService queueService;

    private final PetitionMapper petitionMapper;

    @Override
    public Mono<PetitionEntity> createPetitionByStudent(UUID userId, PetitionDto dto) {
        return studentService.getByUserId(userId)
                .map(StudentEntity::getId)
                .map(studentId -> createPetition(dto, studentId, null))
                .flatMap(petitionRepository::save); // TODO: прикрутить добавление в очередь
    }

    @Override
    public Mono<PetitionEntity> createPetitionByDeputyDean(UUID userId, PetitionDto dto) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .map(deputyDeanId -> createPetition(dto, null, deputyDeanId))
                .flatMap(petitionRepository::save);
    }

    @Override
    public Mono<PetitionEntity> cancelPetition(UUID userId, UUID petitionId) {
        return petitionRepository.findById(petitionId)
                .map(petition -> {
                    petition.setStatus(PetitionStatus.CANCELLED);
                    return petition;
                })
                .flatMap(petitionRepository::save);
    }

    @Override
    public Flux<PetitionEntity> getAllActivePetitions(UUID userId) {
        return studentService.getByUserId(userId)
                .map(StudentEntity::getId)
                .flatMapMany(petitionRepository::findAllByStudentId);
    }

    @Override
    public Mono<PetitionEntity> completePetition(UUID userId, UUID petitionId) {
        return petitionRepository.findById(petitionId)
                .map(petition  -> {
                    petition.setStatus(PetitionStatus.PROCESSED);
                    return petition;
                })
                .flatMap(petitionRepository::save);
    }

    private PetitionEntity createPetition(PetitionDto dto, UUID studentId, UUID deputyDeanId) {
        PetitionEntity entity = petitionMapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        if (studentId != null) {
            entity.setStudentId(studentId);
        }
        if (deputyDeanId != null) {
            entity.setDeputyDeanId(deputyDeanId);
        }
        entity.setNew(true);
        return entity;
    }
}
