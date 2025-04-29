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
    private final PetitionMapper petitionMapper;

    @Override
    public Mono<PetitionEntity> createPetitionByStudent(UUID studentId, PetitionDto dto) {
         return petitionRepository.save(createWaitingPetition(dto, studentId));
    }

    @Override
    public Mono<PetitionEntity> cancelPetition(UUID petitionId) {
        return petitionRepository.findById(petitionId)
                .map(petition -> {
                    petition.setStatus(PetitionStatus.CANCELLED);
                    return petition;
                })
                .flatMap(petitionRepository::save);
    }

    @Override
    public Mono<PetitionEntity> createPetitionByDeputyDean(UUID deputyDeanId, PetitionDto dto) {
        return petitionRepository.save(createProcessedPetition(dto, deputyDeanId));
    }

    @Override
    public Mono<PetitionEntity> completePetition(UUID petitionId) {
        return petitionRepository.findById(petitionId)
                .map(petition  -> {
                    petition.setStatus(PetitionStatus.PROCESSED);
                    petition.setEndedAt(LocalDateTime.now());
                    return petition;
                })
                .flatMap(petitionRepository::save);
    }

    @Override
    public Flux<PetitionEntity> getWaitingStudentInQueueBeforeThisStudent(UUID studentId, UUID dayScheduleId) {
        return petitionRepository.findByStudentIdAndDayScheduleIdAndStatus(studentId, dayScheduleId, PetitionStatus.WAITING)
                .flatMapMany(petitionOfThisStudent -> petitionRepository.findAllByDayScheduleIdAndStatus(dayScheduleId, PetitionStatus.WAITING)
                        .filter(petition -> petition.getCreatedAt().getNano() < petitionOfThisStudent.getCreatedAt().getNano()));
    }

    @Override
    public Mono<PetitionEntity> getFirstWaitingPetitionAfterTimestamp(LocalDateTime timestamp, UUID dayScheduleId) {
        return petitionRepository.findFirstByCreatedAtAndDayScheduleIdAndStatusOrderByCreatedAtAsc(
                timestamp, dayScheduleId, PetitionStatus.WAITING);
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
