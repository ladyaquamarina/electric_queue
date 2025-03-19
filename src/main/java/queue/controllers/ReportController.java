package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queue.services.impl.DayScheduleServiceImpl;
import queue.services.impl.PetitionServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final PetitionServiceImpl petitionService;
    private final DayScheduleServiceImpl dayScheduleService;
}
