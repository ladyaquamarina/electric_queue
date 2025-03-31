package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import queue.dtos.PetitionDto;
import queue.mappers.PetitionMapper;
import queue.models.DeputyDeanEntity;
import queue.models.PetitionEntity;
import queue.models.StudentEntity;
import queue.repositories.PetitionRepository;
import queue.repositories.UserCacheRedisRepository;
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
    private final UserCacheRedisRepository userCacheRedisRepository;  // кэш userId->studentId

    private final StudentService studentService;
    private final DeputyDeanService deputyDeanService;
    private final QueueService queueService;

    private final PetitionMapper petitionMapper;

    @Override
    public Mono<PetitionEntity> createPetitionByStudent(UUID userId, PetitionDto dto) {
        return userCacheRedisRepository.findByUserId(userId)
                .switchIfEmpty(studentService.getByUserId(userId)
                        .map(StudentEntity::getId))
                .map(studentId -> dtoToEntity(dto, studentId, null));
    }

    @Override
    public Mono<PetitionEntity> createPetitionByDeputyDean(UUID userId, PetitionDto dto) {
        return userCacheRedisRepository.findByUserId(userId)
                .switchIfEmpty(deputyDeanService.getByUserId(userId)
                        .map(DeputyDeanEntity::getId))
                .map(deputyDeanId -> dtoToEntity(dto, null, deputyDeanId));
    }

    @Override
    public Mono<PetitionEntity> canselPetition(UUID userId, UUID petitionId) {
        return null;
    }

    @Override
    public Flux<PetitionEntity> getAllActivePetitions(UUID userId) {
        return null;
    }

    @Override
    public Mono<PetitionEntity> completePetition(UUID userId, UUID petitionId) {
        return null;
    }

    private PetitionEntity dtoToEntity(PetitionDto dto, UUID studentId, UUID deputyDeanId) {
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
