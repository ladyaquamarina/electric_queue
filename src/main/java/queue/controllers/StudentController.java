package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queue.mappers.DayScheduleMapper;
import queue.mappers.PetitionMapper;
import queue.mappers.StudentMapper;
import queue.services.DayScheduleService;
import queue.services.PetitionService;
import queue.services.StudentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final PetitionService petitionService;
    private final DayScheduleService dayScheduleService;

    private final PetitionMapper petitionMapper;
    private final StudentMapper studentMapper;
    private final DayScheduleMapper dayScheduleMapper;
}
