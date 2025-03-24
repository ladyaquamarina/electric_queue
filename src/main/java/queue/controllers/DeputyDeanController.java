package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queue.dtos.DayScheduleDto;
import queue.dtos.DeputyDeanDto;
import queue.dtos.PetitionDto;
import queue.mappers.DayScheduleMapper;
import queue.mappers.DeputyDeanMapper;
import queue.mappers.PetitionMapper;
import queue.services.PetitionService;
import queue.services.DeputyDeanService;
import queue.services.DayScheduleService;
import queue.services.QueueService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deputy_dean")
public class DeputyDeanController {
    private final DeputyDeanService deputyDeanService;
    private final DayScheduleService dayScheduleService;
    private final QueueService queueService;
    private final PetitionService petitionService;

    private final PetitionMapper petitionMapper;
    private final DeputyDeanMapper deputyDeanMapper;
    private final DayScheduleMapper dayScheduleMapper;

    @PostMapping("/new")
    public Mono<DeputyDeanDto> createNewDeputyDean(@RequestBody DeputyDeanDto dto) {
        return deputyDeanService.createNewDeputyDean(dto)
                .map(deputyDeanMapper::toDto);
    }

    @PostMapping("/{deputyDeanId}/update")
    public Mono<DeputyDeanDto> updateDeputyDean(
            @PathVariable UUID deputyDeanId,
            @RequestBody DeputyDeanDto dto) {
        return deputyDeanService.updateDeputyDean(deputyDeanId, dto)
                .map(deputyDeanMapper::toDto);
    }

    @PostMapping("/{deputyDeanId}/day_schedule")
    public Mono<DayScheduleDto> createDaySchedule(
            @PathVariable UUID deputyDeanId,
            @RequestBody DayScheduleDto dto) {
        return dayScheduleService.createNewDaySchedule(deputyDeanId, dto)
                .map(dayScheduleMapper::toDto);
    }

    @PostMapping("/{deputyDeanId}/day_schedule/{dayScheduleId}/update")
    public Mono<DayScheduleDto> updateDaySchedule(
            @PathVariable UUID deputyDeanId,
            @RequestBody DayScheduleDto dto) {
        return dayScheduleService.updateDaySchedule(deputyDeanId, dto)
                .map(dayScheduleMapper::toDto);
    }

    @PostMapping("/{deputyDeanId}/day_schedule/{dayScheduleId}/start")
    public Mono<DayScheduleDto> startDaySchadule(
            @PathVariable UUID deputyDeanId,
            @PathVariable UUID dayScheduleId) {
        return dayScheduleService.startDaySchedule(deputyDeanId, dayScheduleId)
                .map(dayScheduleMapper::toDto);
    }

    @PostMapping("/{deputyDeanId}/day_schedule/{dayScheduleId}/end")
    public Mono<DayScheduleDto> endDaySchedule(
            @PathVariable UUID deputyDeanId,
            @PathVariable UUID dayScheduleId) {
        return dayScheduleService.endDaySchedule(deputyDeanId, dayScheduleId)
                .map(dayScheduleMapper::toDto);
    }

    @GetMapping("/{deputyDeanId}/day_schedule/active")
    public Flux<DayScheduleDto> getAllActiveDaySchedule(@PathVariable UUID deputyDeanId) {
        return dayScheduleService.getAllActiveDaySchedule(deputyDeanId)
                .map(dayScheduleMapper::toDto);
    }

    @GetMapping("/{deputyDeanId}/day_schedule/canceled")
    public Flux<DayScheduleDto> getAllCanceledDaySchedule(@PathVariable UUID deputyDeanId) {
        return dayScheduleService.getAllCanceledDaySchedule(deputyDeanId)
                .map(dayScheduleMapper::toDto);
    }

    @PostMapping("/{deputyDeanId}/petition")
    public Mono<PetitionDto> createPetition(
            @PathVariable UUID deputyDeanId,
            @RequestBody PetitionDto dto) {
        return petitionService.createPetitionByDeputyDean(deputyDeanId, dto)
                .map(petitionMapper::toDto);
    }

    //возвращает следующее в очереди обращение
    @PostMapping("/{deputyDeanId}/petition/{petitionId}/complete")
    public Mono<PetitionDto> completePetition(
            @PathVariable UUID deputyDeanId,
            @PathVariable UUID petitionId) {
        return petitionService.completePetition(deputyDeanId, petitionId)
                .map(petitionMapper::toDto);
    }
}
