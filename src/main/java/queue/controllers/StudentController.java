package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queue.dtos.StudentDto;
import queue.mappers.DayScheduleMapper;
import queue.mappers.PetitionMapper;
import queue.mappers.StudentMapper;
import queue.services.impl.DayScheduleServiceImpl;
import queue.services.impl.PetitionServiceImpl;
import queue.services.impl.StudentServiceImpl;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final StudentServiceImpl studentService;
    private final PetitionServiceImpl petitionService;
    private final DayScheduleServiceImpl dayScheduleService;

    private final StudentMapper studentMapper;
    private final PetitionMapper petitionMapper;
    private final DayScheduleMapper dayScheduleMapper;

    @RequestMapping("/new")
    public Mono<StudentDto> createNewStudent(@RequestBody StudentDto dto) {
        return studentService.createNewStudent(dto)
                .map(studentMapper::toDto);
    }

    @RequestMapping("/update")
    public Mono<StudentDto> updateStudent(@RequestBody StudentDto dto) {
        return studentService.updateStudent(dto)
                .map(studentMapper::toDto);
    }
}
