package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.dtos.DayScheduleDto;
import queue.mappers.DayScheduleMapper;
import queue.models.DayScheduleEntity;
import queue.models.DeputyDeanEntity;
import queue.repositories.DayScheduleRepository;
import queue.services.DayScheduleService;
import queue.services.DeputyDeanService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static queue.utils.Utils.SUCCESS;

@Service
@RequiredArgsConstructor
public class DayScheduleServiceImpl implements DayScheduleService {
    private final DayScheduleRepository dayScheduleRepository;

    private final DeputyDeanService deputyDeanService;

    private final DayScheduleMapper dayScheduleMapper;

    @Override
    public Mono<DayScheduleEntity> createNewDaySchedule(UUID userId, DayScheduleDto dto) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .map(deputyDeanId -> createDateSchedule(dto, deputyDeanId))
                .flatMap(dayScheduleRepository::save);
    }

    @Override
    public Flux<DayScheduleEntity> getDaySchedulesByUserId(UUID userId) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMapMany(this::getDaySchedulesByDeputyDean);
    }

    @Override
    public Flux<DayScheduleEntity> getDaySchedulesByDeputyDean(UUID deputyDeanId) {
        return dayScheduleRepository.findAllByDeputyDeanId(deputyDeanId);
    }

    @Override
    public Mono<DayScheduleEntity> updateDaySchedule(UUID userId, UUID dayScheduleId, DayScheduleDto dto) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMap(deputyDeanId -> dayScheduleRepository.findById(dayScheduleId)
                        .map(daySchedule -> dayScheduleMapper.update(daySchedule, dto))
                        .flatMap(dayScheduleRepository::save));
    }

    @Override
    public Mono<DayScheduleEntity> startDaySchedule(UUID userId, UUID dayScheduleId) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMap(deputyDeanId -> dayScheduleRepository.findById(dayScheduleId)
                        .map(daySchedule ->{
                            daySchedule.setStartAtFact(LocalDateTime.now());
                            return daySchedule;
                        })
                        .flatMap(dayScheduleRepository::save));
    }

    @Override
    public Mono<DayScheduleEntity> endDaySchedule(UUID userId, UUID dayScheduleId) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMap(deputyDeanId -> dayScheduleRepository.findById(dayScheduleId)
                        .map(daySchedule ->{
                            daySchedule.setEndAtFact(LocalDateTime.now());
                            return daySchedule;
                        })
                        .flatMap(dayScheduleRepository::save));
    }

    @Override
    public Mono<String> deleteDaySchedule(UUID userId, UUID dayScheduleId) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMap(deputyDeanId -> dayScheduleRepository.deleteById(dayScheduleId)
                        .then(Mono.just(SUCCESS)));
    }

    private DayScheduleEntity createDateSchedule(DayScheduleDto dto, UUID deputyDeanId) {
        DayScheduleEntity entity = dayScheduleMapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        entity.setDeputyDeanId(deputyDeanId);
        entity.setNew(true);
        return entity;
    }
}
