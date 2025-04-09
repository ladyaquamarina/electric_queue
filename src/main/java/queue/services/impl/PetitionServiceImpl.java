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

import java.time.LocalDateTime;
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
                .map(studentId -> createWaitingPetition(dto, studentId))
                .flatMap(petitionRepository::save); // TODO: прикрутить добавление в очередь
    }

    @Override
    public Mono<PetitionEntity> createPetitionByDeputyDean(UUID userId, PetitionDto dto) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .map(deputyDeanId -> createProcessedPetition(dto, deputyDeanId))
                .flatMap(petitionRepository::save);
    }

    @Override
    public Mono<PetitionEntity> cancelPetition(UUID userId, UUID petitionId) {
        return studentService.getByUserId(userId)
                .map(StudentEntity::getId)
                .flatMap(studentId -> petitionRepository.findById(petitionId))
                .map(petition -> {
                    petition.setStatus(PetitionStatus.CANCELLED);
                    return petition;
                })
                .flatMap(petitionRepository::save); // TODO: прикрутить исключение из очереди
    }

    @Override
    public Mono<PetitionEntity> completePetition(UUID userId, UUID petitionId) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMap(deputyDeanId -> petitionRepository.findById(petitionId))
                .map(petition  -> {
                    petition.setStatus(PetitionStatus.PROCESSED);
                    petition.setEndedAt(LocalDateTime.now());
                    return petition;
                })
                .flatMap(petitionRepository::save); // TODO: прикрутить исключение из очереди (сдвиг очереди)
    }

    @Override
    public Mono<Long> getNumberInQueue(UUID userId, UUID dayScheduleId) {
        return studentService.getByUserId(userId)
                .map(StudentEntity::getId)
                .flatMapMany(studentId -> getWaitingStudentInQueueBeforeThisStudent(studentId, dayScheduleId))
                .count();
    }

    @Override
    public Flux<PetitionEntity> getAllActivePetitions(UUID dayScheduleId) {
        return petitionRepository.findAllByDayScheduleIdAndStatus(dayScheduleId, PetitionStatus.WAITING);
    }

    @Override
    public Flux<PetitionEntity> cancelAllWaitingPetitions(UUID dayScheduleId) {
        return petitionRepository.findAllByDayScheduleIdAndStatus(dayScheduleId, PetitionStatus.WAITING)
                .map(petition -> {
                    petition.setStatus(PetitionStatus.NOT_PROCESSED);
                    return petition;
                })
                .flatMap(petitionRepository::save);
    }

    private Flux<PetitionEntity> getWaitingStudentInQueueBeforeThisStudent(UUID studentId, UUID dayScheduleId) {
        return petitionRepository.findByStudentIdAndDayScheduleIdAndStatus(studentId, dayScheduleId, PetitionStatus.WAITING)
                .flatMapMany(petitionOfThisStudent -> petitionRepository.findAllByDayScheduleIdAndStatus(dayScheduleId, PetitionStatus.WAITING)
                        .filter(petition -> petition.getCreatedAt().getNano() < petitionOfThisStudent.getCreatedAt().getNano()));
    }

    private PetitionEntity createWaitingPetition(PetitionDto dto, UUID studentId) {
        PetitionEntity entity = createPetition(dto, studentId, null);
        entity.setStatus(PetitionStatus.WAITING);
        return entity;
    }

    private PetitionEntity createProcessedPetition(PetitionDto dto, UUID deputyDeanId) {
        PetitionEntity entity = createPetition(dto, null, deputyDeanId);
        entity.setStatus(PetitionStatus.PROCESSED);
        entity.setEndedAt(LocalDateTime.now());
        return entity;
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
        entity.setCreatedAt(LocalDateTime.now());
        entity.setNew(true);
        return entity;
    }
}
