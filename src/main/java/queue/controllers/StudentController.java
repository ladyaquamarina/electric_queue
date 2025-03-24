package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queue.dtos.DayScheduleDto;
import queue.dtos.PetitionDto;
import queue.dtos.StudentDto;
import queue.mappers.DayScheduleMapper;
import queue.mappers.PetitionMapper;
import queue.mappers.StudentMapper;
import queue.services.DayScheduleService;
import queue.services.PetitionService;
import queue.services.StudentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static queue.utils.Utils.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final DayScheduleService dayScheduleService;
    private final PetitionService petitionService;

    private final PetitionMapper petitionMapper;
    private final StudentMapper studentMapper;
    private final DayScheduleMapper dayScheduleMapper;

    @PostMapping("/new")
    public Mono<StudentDto> createNewStudent(@RequestBody StudentDto dto) {
        return studentService.createNewStudent(dto)
                .map(studentMapper::toDto);
    }

    @PostMapping("/{studentId}/update")
    public Mono<StudentDto> updateStudent(
            @PathVariable UUID studentId,
            @RequestBody StudentDto dto) {
        return studentService.updateStudent(studentId, dto)
                .map(studentMapper::toDto);
    }

    @PostMapping("/{studentId}/petition")
    public Mono<PetitionDto> createPetition(
            @PathVariable UUID studentId,
            @RequestBody PetitionDto dto) {
        return petitionService.createPetitionByStudent(studentId, dto)
                .map(petitionMapper::toDto);
    }

    @DeleteMapping("/{studentId}/petition/{petitionId}")
    public Mono<String> cancelPetition(
            @PathVariable UUID studentId,
            @PathVariable UUID petitionId) {
        return petitionService.canselPetition(studentId, petitionId)
                .map(petition -> SUCCESS);
    }

    @GetMapping("/{studentId}/petition/all")
    public Flux<PetitionDto> getAllActivePetitions(@PathVariable UUID studentId) {
        return petitionService.getAllActivePetitions(studentId)
                .map(petitionMapper::toDto);
    }

    @GetMapping("/{studentId}/day_schedule/{deputyDeanId}")
    public Flux<DayScheduleDto> getDaySchedules(
            @PathVariable UUID studentId,
            @PathVariable UUID deputyDeanId) {
        return dayScheduleService.getDaySchedules(studentId, deputyDeanId)
                .map(dayScheduleMapper::toDto);
    }
}
