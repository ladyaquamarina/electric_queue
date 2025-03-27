package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.dtos.DayScheduleDto;
import queue.models.DayScheduleEntity;
import queue.repositories.DayScheduleRepository;
import queue.services.DayScheduleService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DayScheduleServiceImpl implements DayScheduleService {
    private final DayScheduleRepository dayScheduleRepository;

    @Override
    public Mono<DayScheduleEntity> createNewDaySchedule(UUID userId, DayScheduleDto dto) {
        return null;
    }

    @Override
    public Flux<DayScheduleEntity> getDaySchedules(UUID userId, UUID deputyDeanId) {
        return null;
    }

    @Override
    public Flux<DayScheduleEntity> getAllActiveDaySchedule(UUID deputyDeanId) {
        return null;
    }

    @Override
    public Flux<DayScheduleEntity> getAllCanceledDaySchedule(UUID deputyDeanId) {
        return null;
    }

    @Override
    public Mono<DayScheduleEntity> updateDaySchedule(UUID deputyDeanId, DayScheduleDto dto) {
        return null;
    }

    @Override
    public Mono<DayScheduleEntity> startDaySchedule(UUID deputyDeanId, UUID dayScheduleId) {
        return null;
    }

    @Override
    public Mono<DayScheduleEntity> endDaySchedule(UUID deputyDeanId, UUID dayScheduleId) {
        return null;
    }
}
