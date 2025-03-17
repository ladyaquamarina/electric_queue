package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queue.services.DayScheduleService;
import queue.services.PetitionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final PetitionService petitionService;
    private final DayScheduleService dayScheduleService;
}
