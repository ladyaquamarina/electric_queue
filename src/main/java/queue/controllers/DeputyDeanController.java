package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import queue.dtos.DayScheduleDto;
import queue.dtos.DeputyDeanDto;
import queue.dtos.PetitionDto;
import queue.mappers.DayScheduleMapper;
import queue.mappers.DeputyDeanMapper;
import queue.mappers.PetitionMapper;
import queue.services.AuthenticationService;
import queue.services.PetitionService;
import queue.services.DeputyDeanService;
import queue.services.DayScheduleService;
import queue.services.QueueService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static queue.utils.Utils.authError;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deputy_dean")
public class DeputyDeanController {
    private final DeputyDeanService deputyDeanService;
    private final DayScheduleService dayScheduleService;
    private final QueueService queueService;
    private final PetitionService petitionService;
    private final AuthenticationService authenticationService;

    private final PetitionMapper petitionMapper;
    private final DeputyDeanMapper deputyDeanMapper;
    private final DayScheduleMapper dayScheduleMapper;

    @PostMapping("/new")
    public Mono<DeputyDeanDto> createNewDeputyDean(@RequestParam Long chatId,
                                                   @RequestBody DeputyDeanDto dto) {
        return deputyDeanService.createNewDeputyDean(chatId, dto)
                .map(deputyDeanMapper::toDto);
    }

    @PostMapping("/update")
    public Mono<DeputyDeanDto> updateDeputyDean(@RequestParam Long chatId,
                                                @RequestParam UUID userId,
                                                @RequestBody DeputyDeanDto dto) {
        return authenticationService.checkChatIdMatchUserId(chatId, userId)
                .flatMap(check -> deputyDeanService.updateDeputyDean(chatId, dto))
                .map(deputyDeanMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @PostMapping("/day_schedule/new")
    public Mono<DayScheduleDto> createDaySchedule(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @RequestBody DayScheduleDto dto) {
        return dayScheduleService.createNewDaySchedule(deputyDeanId, dto)
                .map(dayScheduleMapper::toDto);
    }

    @PostMapping("/day_schedule/update")
    public Mono<DayScheduleDto> updateDaySchedule(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @RequestBody DayScheduleDto dto) {
        return dayScheduleService.updateDaySchedule(deputyDeanId, dto)
                .map(dayScheduleMapper::toDto);
    }

    @PostMapping("/day_schedule/start")
    public Mono<DayScheduleDto> startDaySchadule(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @PathVariable UUID dayScheduleId) {
        return dayScheduleService.startDaySchedule(deputyDeanId, dayScheduleId)
                .map(dayScheduleMapper::toDto);
    }

    @PostMapping("/day_schedule/end")
    public Mono<DayScheduleDto> endDaySchedule(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @PathVariable UUID deputyDeanId,
            @PathVariable UUID dayScheduleId) {
        return dayScheduleService.endDaySchedule(deputyDeanId, dayScheduleId)
                .map(dayScheduleMapper::toDto);
    }

    @GetMapping("/day_schedule/active")
    public Flux<DayScheduleDto> getAllActiveDaySchedule(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @PathVariable UUID deputyDeanId) {
        return dayScheduleService.getAllActiveDaySchedule(deputyDeanId)
                .map(dayScheduleMapper::toDto);
    }

    @GetMapping("/day_schedule/canceled")
    public Flux<DayScheduleDto> getAllCanceledDaySchedule(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @PathVariable UUID deputyDeanId) {
        return dayScheduleService.getAllCanceledDaySchedule(deputyDeanId)
                .map(dayScheduleMapper::toDto);
    }

    @PostMapping("/petition")
    public Mono<PetitionDto> createPetition(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @PathVariable UUID deputyDeanId,
            @RequestBody PetitionDto dto) {
        return petitionService.createPetitionByDeputyDean(deputyDeanId, dto)
                .map(petitionMapper::toDto);
    }

    //возвращает следующее в очереди обращение
    @PostMapping("/petition/complete")
    public Mono<PetitionDto> completePetition(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @RequestParam UUID petitionId) {
        return petitionService.completePetition(deputyDeanId, petitionId)
                .map(petitionMapper::toDto);
    }
}
