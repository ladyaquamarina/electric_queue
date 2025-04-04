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
import queue.mappers.DayScheduleMapper;
import queue.mappers.PetitionMapper;
import queue.mappers.StudentMapper;
import queue.services.AuthenticationService;
import queue.services.DayScheduleService;
import queue.services.PetitionService;
import queue.services.StudentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static queue.utils.Utils.SUCCESS;
import static queue.utils.Utils.authError;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final DayScheduleService dayScheduleService;
    private final PetitionService petitionService;
    private final AuthenticationService authenticationService;

    private final PetitionMapper petitionMapper;
    private final StudentMapper studentMapper;
    private final DayScheduleMapper dayScheduleMapper;

    @PostMapping("/new")
    public Mono<StudentDto> createNewStudent(@RequestParam Long chatId,
                                             @RequestBody StudentDto dto) {
        return studentService.createNewStudent(chatId, dto)
                .map(studentMapper::toDto);
    }

    @PostMapping("/update")
    public Mono<StudentDto> updateStudent(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @RequestBody StudentDto dto) {
        return authenticationService.checkChatIdMatchUserId(chatId, userId)
                .flatMap(check -> studentService.updateStudent(chatId, dto))
                .map(studentMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @PostMapping("/petition")
    public Mono<PetitionDto> createPetition(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @RequestBody PetitionDto dto) {
        return authenticationService.checkChatIdMatchUserId(chatId, userId)
                .flatMap(check -> petitionService.createPetitionByStudent(userId, dto))
                .map(petitionMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @DeleteMapping("/petition")
    public Mono<String> cancelPetition(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @RequestParam UUID petitionId) {
        return authenticationService.checkChatIdMatchUserId(chatId, userId)
                .flatMap(check -> petitionService.canselPetition(userId, petitionId))
                .map(petition -> SUCCESS)
                .switchIfEmpty(Mono.error(authError()));
    }

    @GetMapping("/petition/all")
    public Flux<PetitionDto> getAllActivePetitions(
            @RequestParam Long chatId,
            @RequestParam UUID userId) {
        return authenticationService.checkChatIdMatchUserId(chatId, userId)
                .flatMapMany(check -> petitionService.getAllActivePetitions(userId))
                .map(petitionMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }

    @GetMapping("/day_schedule/{deputyDeanId}")
    public Flux<DayScheduleDto> getDaySchedules(
            @RequestParam Long chatId,
            @RequestParam UUID userId,
            @PathVariable UUID deputyDeanId) {
        return authenticationService.checkChatIdMatchUserId(chatId, userId)
                .flatMapMany(check -> dayScheduleService.getDaySchedules(userId, deputyDeanId))
                .map(dayScheduleMapper::toDto)
                .switchIfEmpty(Mono.error(authError()));
    }
}
