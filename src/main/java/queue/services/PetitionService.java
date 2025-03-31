package queue.services;

import queue.dtos.PetitionDto;
import queue.models.DayScheduleEntity;
import queue.models.DeputyDeanEntity;
import queue.models.PetitionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PetitionService {
    Mono<PetitionEntity> createPetitionByStudent(UUID userId, PetitionDto dto);

    Mono<PetitionEntity> createPetitionByDeputyDean(UUID userId, PetitionDto dto);

    Mono<PetitionEntity> canselPetition(UUID userId, UUID petitionId);

    Flux<PetitionEntity> getAllActivePetitions(UUID userId);

    Mono<PetitionEntity> completePetition(UUID userId, UUID petitionId);
}
