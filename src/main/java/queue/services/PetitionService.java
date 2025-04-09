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

    Mono<PetitionEntity> cancelPetition(UUID userId, UUID petitionId);

    Mono<PetitionEntity> completePetition(UUID userId, UUID petitionId);

    Mono<Long> getNumberInQueue(UUID userId, UUID dayScheduleId);


    Flux<PetitionEntity> getAllActivePetitions(UUID dayScheduleId);

    Flux<PetitionEntity> cancelAllWaitingPetitions(UUID dayScheduleId);
}
