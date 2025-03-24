package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.dtos.PetitionDto;
import queue.models.DayScheduleEntity;
import queue.models.DeputyDeanEntity;
import queue.models.PetitionEntity;
import queue.repositories.PetitionRepository;
import queue.services.PetitionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetitionServiceImpl implements PetitionService {
    private final PetitionRepository petitionRepository;
    private final QueueServiceImpl queueService;

    @Override
    public Mono<PetitionEntity> createPetitionByStudent(UUID studentId, PetitionDto dto) {
        return null;
    }

    @Override
    public Mono<PetitionEntity> createPetitionByDeputyDean(UUID deputyDeanId, PetitionDto dto) {
        return null;
    }

    @Override
    public Mono<PetitionEntity> canselPetition(UUID studentId, UUID petitionId) {
        return null;
    }

    @Override
    public Flux<PetitionEntity> getAllActivePetitions(UUID studentId) {
        return null;
    }

    @Override
    public Mono<PetitionEntity> completePetition(UUID deputyDeanId, UUID petitionId) {
        return null;
    }
}
