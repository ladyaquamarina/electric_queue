package queue.services;

import queue.dtos.DayScheduleDto;
import queue.models.DayScheduleEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DayScheduleService {

    Mono<DayScheduleEntity> createNewDaySchedule(UUID userId, DayScheduleDto dto);

    Flux<DayScheduleEntity> getDaySchedules(UUID studentId, UUID deputyDeanId);

    Flux<DayScheduleEntity> getAllActiveDaySchedule(UUID deputyDeanId);

    Flux<DayScheduleEntity> getAllCanceledDaySchedule(UUID deputyDeanId);

    Mono<DayScheduleEntity> updateDaySchedule(UUID deputyDeanId, DayScheduleDto dto);

    Mono<DayScheduleEntity> startDaySchedule(UUID deputyDeanId, UUID dayScheduleId);

    Mono<DayScheduleEntity> endDaySchedule(UUID deputyDeanId, UUID dayScheduleId);
}
