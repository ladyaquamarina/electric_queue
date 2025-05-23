package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.dtos.DayScheduleDto;
import queue.mappers.DayScheduleMapper;
import queue.models.DayScheduleEntity;
import queue.models.DeputyDeanEntity;
import queue.models.StudentEntity;
import queue.repositories.DayScheduleRepository;
import queue.services.DayScheduleService;
import queue.services.DeputyDeanService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
    public Mono<DayScheduleEntity> startDaySchedule(UUID dayScheduleId) {
        return dayScheduleRepository.findById(dayScheduleId)
                .map(daySchedule ->{
                    daySchedule.setStartAtFact(LocalTime.now());
                    return daySchedule;
                })
                .flatMap(dayScheduleRepository::save);
    }

    @Override
    public Mono<DayScheduleEntity> endDaySchedule(UUID dayScheduleId) {
        return dayScheduleRepository.findById(dayScheduleId)
                .map(daySchedule ->{
                    daySchedule.setEndAtFact(LocalTime.now());
                    return daySchedule;
                })
                .flatMap(dayScheduleRepository::save);
    }

    @Override
    public Mono<String> deleteDaySchedule(UUID userId, UUID dayScheduleId) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMap(deputyDeanId -> dayScheduleRepository.deleteById(dayScheduleId)
                        .then(Mono.just(SUCCESS)));
    }

    @Override
    public Flux<DayScheduleEntity> getFromTimeInterval(LocalDate start, LocalDate end, UUID deputyDeanId) {
        return dayScheduleRepository.findAllByDeputyDeanIdAndDateBetween(deputyDeanId, start, end);
    }

    @Override
    public Mono<DayScheduleEntity> getById(UUID dayScheduleId) {
        return dayScheduleRepository.findById(dayScheduleId);
    }

    @Override
    public Flux<DayScheduleEntity> getByDayScheduleIds(List<UUID> ids) {
        return dayScheduleRepository.findAllByIds(ids)
                .collectList()
                .flatMap(this::fillDeputyDeanInScheduleList)
                .flatMapMany(Flux::fromIterable);
    }

    private Mono<List<DayScheduleEntity>> fillDeputyDeanInScheduleList(List<DayScheduleEntity> daySchedules) {
        return deputyDeanService.getByDeputyDeanIds(daySchedules.stream().map(DayScheduleEntity::getId).toList())
                .map(deputyDean -> {
                    for (DayScheduleEntity daySchedule : daySchedules){
                        if (daySchedule.getDeputyDeanId() == deputyDean.getId()) {
                            daySchedule.setDeputyDean(deputyDean);
                            break;
                        }
                    }
                    return deputyDean;
                })
                .collectList()
                .map(users -> daySchedules);
    }

    private DayScheduleEntity createDateSchedule(DayScheduleDto dto, UUID deputyDeanId) {
        DayScheduleEntity entity = dayScheduleMapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        entity.setDeputyDeanId(deputyDeanId);
        entity.setNew(true);
        return entity;
    }
}
