package queue.services;

import queue.dtos.PetitionDto;
import queue.models.DayScheduleEntity;
import queue.models.DeputyDeanEntity;
import queue.models.PetitionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PetitionService {
    Mono<PetitionEntity> createPetitionByStudent(UUID studentId, PetitionDto dto);

    Mono<PetitionEntity> createPetitionByDeputyDean(UUID deputyDeanId, PetitionDto dto);

    Mono<PetitionEntity> canselPetition(UUID studentId, UUID petitionId);

    Flux<PetitionEntity> getAllActivePetitions(UUID studentId);

    Mono<PetitionEntity> completePetition(UUID deputyDeanId, UUID petitionId);
}
