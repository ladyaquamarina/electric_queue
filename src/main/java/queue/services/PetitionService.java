package queue.services;

import queue.dtos.NameValueDto;
import queue.dtos.PetitionDto;
import queue.models.PetitionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface PetitionService {
    // используется студентами
    Mono<PetitionEntity> createPetitionByStudent(UUID studentId, PetitionDto dto);
    Mono<PetitionEntity> cancelPetition(UUID petitionId);

    Mono<PetitionEntity> createPetitionByDeputyDean(UUID deputyDeanId, PetitionDto dto);
    Mono<PetitionEntity> completePetition(UUID petitionId);
    Flux<PetitionEntity> getWaitingStudentInQueueBeforeThisStudent(UUID studentId, UUID dayScheduleId);
    Mono<PetitionEntity> getFirstWaitingPetitionAfterTimestamp(LocalDateTime timestamp, UUID dayScheduleId);
    Mono<PetitionEntity> getFirstWaitingPetition(UUID dayScheduleId);

    Flux<PetitionEntity> getAllActivePetitions(UUID dayScheduleId);
    Flux<PetitionEntity> cancelAllWaitingPetitions(UUID dayScheduleId);

    Flux<PetitionEntity> getAllByStartDateAndEndDate(
            LocalDate startDate, LocalDate endDate, UUID deputyDeanId, NameValueDto purpose);
}
