package queue.services;

import queue.dtos.DayScheduleDto;
import queue.models.DayScheduleEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DayScheduleService {

    Mono<DayScheduleEntity> createNewDaySchedule(UUID userId, DayScheduleDto dto);

    Flux<DayScheduleEntity> getDaySchedulesByUserId(UUID userId);

    Flux<DayScheduleEntity> getDaySchedulesByDeputyDean(UUID deputyDeanId);

    Mono<DayScheduleEntity> updateDaySchedule(UUID userId, UUID dayScheduleId, DayScheduleDto dto);

    Mono<DayScheduleEntity> startDaySchedule(UUID userId, UUID dayScheduleId);

    Mono<DayScheduleEntity> endDaySchedule(UUID userId, UUID dayScheduleId);

    Mono<String> deleteDaySchedule(UUID userId, UUID dayScheduleId);
}
