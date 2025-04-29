package queue.services;

import queue.dtos.PetitionDto;
import queue.models.DayScheduleEntity;
import queue.models.DeputyDeanEntity;
import queue.models.PetitionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PetitionService {
    // используется студентами
    Mono<PetitionEntity> createPetitionByStudent(UUID studentId, PetitionDto dto);
    Mono<PetitionEntity> cancelPetition(UUID petitionId);

    //  используется замдеками
    Mono<PetitionEntity> createPetitionByDeputyDean(UUID deputyDeanId, PetitionDto dto);
    Mono<PetitionEntity> completePetition(UUID petitionId);

    // прочее вспомогательное
    Flux<PetitionEntity> getWaitingStudentInQueueBeforeThisStudent(UUID studentId, UUID dayScheduleId);
    Mono<PetitionEntity> getFirstWaitingPetitionAfterTimestamp(LocalDateTime timestamp, UUID dayScheduleId);
    Flux<PetitionEntity> getAllActivePetitions(UUID dayScheduleId);
    Flux<PetitionEntity> cancelAllWaitingPetitions(UUID dayScheduleId);
}
