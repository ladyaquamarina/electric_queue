package queue.services;

import queue.dtos.DayScheduleDto;
import queue.models.DayScheduleEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DayScheduleService {

    Mono<DayScheduleEntity> createNewDaySchedule(UUID userId, DayScheduleDto dto);
    Flux<DayScheduleEntity> getDaySchedulesByUserId(UUID userId);
    Flux<DayScheduleEntity> getDaySchedulesByDeputyDean(UUID deputyDeanId);
    Mono<DayScheduleEntity> updateDaySchedule(UUID userId, UUID dayScheduleId, DayScheduleDto dto);
    Mono<DayScheduleEntity> startDaySchedule(UUID dayScheduleId);
    Mono<DayScheduleEntity> endDaySchedule(UUID dayScheduleId);
    Mono<String> deleteDaySchedule(UUID userId, UUID dayScheduleId);

    Flux<DayScheduleEntity> getFromTimeInterval(LocalDate start, LocalDate end, UUID deputyDeanId);
    Mono<DayScheduleEntity> getById(UUID dayScheduleId);
    Flux<DayScheduleEntity> getByDayScheduleIds(List<UUID> ids);
}
