package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import static queue.utils.Utils.SUCCESS;
import static queue.utils.Utils.authError;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deputy_dean")
public class DeputyDeanController {
    private final DeputyDeanService deputyDeanService;
    private final DayScheduleService dayScheduleService;
    private final QueueService queueService;

    private final AuthenticationService authenticationService;

    private final PetitionMapper petitionMapper;
    private final DeputyDeanMapper deputyDeanMapper;
    private final DayScheduleMapper dayScheduleMapper;

    @PostMapping("/new")
    public Mono<DeputyDeanDto> createNewDeputyDean(@RequestParam Long chatId,
                                                   @RequestBody DeputyDeanDto dto) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> deputyDeanService.createNewDeputyDean(userId, dto))
                .map(deputyDeanMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @PostMapping("/update")
    public Mono<DeputyDeanDto> updateDeputyDean(@RequestParam Long chatId,
                                                @RequestBody DeputyDeanDto dto) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> deputyDeanService.updateDeputyDean(chatId, dto))
                .map(deputyDeanMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @PostMapping("/day_schedule/new")
    public Mono<DayScheduleDto> createDaySchedule(@RequestParam Long chatId,
                                                  @RequestParam UUID deputyDeanId,
                                                  @RequestBody DayScheduleDto dto) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> dayScheduleService.createNewDaySchedule(deputyDeanId, dto))
                .map(dayScheduleMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @PostMapping("/day_schedule/update")
    public Mono<DayScheduleDto> updateDaySchedule(@RequestParam Long chatId,
                                                  @RequestParam UUID dayScheduleId,
                                                  @RequestBody DayScheduleDto dto) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> dayScheduleService.updateDaySchedule(userId, dayScheduleId, dto))
                .map(dayScheduleMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @PostMapping("/day_schedule/start")
    public Mono<PetitionDto> startDaySchedule(@RequestParam Long chatId,
                                                 @RequestParam UUID dayScheduleId) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> queueService.startQueue(userId, dayScheduleId))
                .map(petitionMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @PostMapping("/day_schedule/end")
    public Mono<String> endDaySchedule(@RequestParam Long chatId,
                                               @RequestParam UUID dayScheduleId) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMapMany(userId -> queueService.endQueue(userId, dayScheduleId))
                .collectList()
                .map(list -> SUCCESS)
                .switchIfEmpty(Mono.error(authError()));
    }

    @GetMapping("/day_schedule/active")
    public Flux<DayScheduleDto> getAllDaySchedule(@RequestParam Long chatId) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMapMany(dayScheduleService::getDaySchedulesByUserId)
                .map(dayScheduleMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @DeleteMapping("/day_schedule")
    public Mono<String> deleteDaySchedule(
            @RequestParam Long chatId,
            @RequestParam UUID dayScheduleId) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> dayScheduleService.deleteDaySchedule(userId, dayScheduleId))
                .switchIfEmpty(Mono.error(authError()));
    }

    @PostMapping("/petition")
    public Mono<PetitionDto> createPetition(
            @RequestParam Long chatId,
            @RequestBody PetitionDto dto) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> queueService.addPetitionBypassingQueue(userId, dto))
                .map(petitionMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    //возвращает следующее в очереди обращение
    @PostMapping("/petition/complete")
    public Mono<PetitionDto> completeAndGetNextPetition(
            @RequestParam Long chatId,
            @RequestParam UUID petitionId) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> queueService.completeAndGetNextPetition(userId, petitionId))
                .map(petitionMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }
}
