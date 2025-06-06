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
import queue.dtos.PetitionDto;
import queue.dtos.StudentDto;
import queue.enums.NumberOfPeopleType;
import queue.enums.PetitionPurpose;
import queue.mappers.DayScheduleMapper;
import queue.mappers.PetitionMapper;
import queue.mappers.StudentMapper;
import queue.models.DayScheduleEntity;
import queue.services.AuthenticationService;
import queue.services.DayScheduleService;
import queue.services.PetitionService;
import queue.services.QueueService;
import queue.services.StudentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.UUID;

import static queue.utils.Utils.SUCCESS;
import static queue.utils.Utils.authError;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final DayScheduleService dayScheduleService;
    private final QueueService queueService;

    private final AuthenticationService authenticationService;

    private final PetitionMapper petitionMapper;
    private final StudentMapper studentMapper;
    private final DayScheduleMapper dayScheduleMapper;

    @PostMapping("/new")
    public Mono<StudentDto> createNewStudent(@RequestParam Long chatId,
                                             @RequestBody StudentDto dto) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> studentService.createNewStudent(userId, dto))
                .map(studentMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @PostMapping("/update")
    public Mono<StudentDto> updateStudent(
            @RequestParam Long chatId,
            @RequestBody StudentDto dto) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> studentService.updateStudent(chatId, dto))
                .map(studentMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @PostMapping("/petition")
    public Mono<PetitionDto> createPetition(
            @RequestParam Long chatId,
            @RequestBody PetitionDto dto) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> queueService.addToQueue(userId, dto))
                .map(petitionMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @DeleteMapping("/petition")
    public Mono<String> cancelPetition(
            @RequestParam Long chatId,
            @RequestParam UUID petitionId) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> queueService.removeFromQueue(userId, petitionId))
                .map(petition -> SUCCESS)
                .switchIfEmpty(Mono.error(authError()));
    }

    @GetMapping("/number_in_queue")
    public Mono<Long> getNumberInQueue(@RequestParam Long chatId,
                                       @RequestParam UUID dayScheduleId) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> queueService.getNumberInQueue(userId, dayScheduleId))
                .switchIfEmpty(Mono.error(authError()));
    }

    @GetMapping("/day_schedule/{deputyDeanId}")
    public Flux<DayScheduleDto> getDaySchedules(
            @RequestParam Long chatId,
            @PathVariable UUID deputyDeanId) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMapMany(userId -> dayScheduleService.getDaySchedulesByDeputyDean(deputyDeanId))
                .map(dayScheduleMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @GetMapping("/prediction")
    public Mono<LinkedHashMap<DayScheduleDto, String>> predictBestDaysForVisit(
            @RequestParam PetitionPurpose purpose,
            @RequestParam UUID deputyDeanId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return queueService.predict3BestDayForVisit(purpose, deputyDeanId, start, end)
                .map(list -> {
                    LinkedHashMap<DayScheduleDto, String> resultMap = new LinkedHashMap<>();
                    list.keySet().iterator().forEachRemaining(dayScheduleEntity ->
                            resultMap.put(dayScheduleMapper.toDto(dayScheduleEntity), list.get(dayScheduleEntity).getValue()));
                    return resultMap;
                });
    }
}
