package queue.services;

import queue.dtos.PetitionDto;
import queue.enums.NumberOfPeopleType;
import queue.enums.PetitionPurpose;
import queue.models.DayScheduleEntity;
import queue.models.PetitionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.UUID;

public interface QueueService {
    Mono<PetitionEntity> addToQueue(UUID userId, PetitionDto petitionDto);
    Mono<PetitionEntity> removeFromQueue(UUID userId, UUID petitionId);
    Mono<Long> getNumberInQueue(UUID userId, UUID dayScheduleId);
    Mono<PetitionEntity> startQueue(UUID userId, UUID dayScheduleId);
    Flux<PetitionEntity> endQueue(UUID userId, UUID dayScheduleId);
    Mono<PetitionEntity> completeAndGetNextPetition(UUID userId, UUID dayScheduleId);
    Mono<PetitionEntity> addPetitionBypassingQueue(UUID userId, PetitionDto petitionDto);


    // выбирает три наименее людных дня для посещения замдекана в заданном промежутке времени
    Mono<LinkedHashMap<DayScheduleEntity, NumberOfPeopleType>> predict3BestDayForVisit(
            PetitionPurpose purpose, UUID deputyDeanId, LocalDate start, LocalDate end);
}
