package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queue.dtos.ReportDto;
import queue.services.impl.DayScheduleServiceImpl;
import queue.services.impl.PetitionServiceImpl;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final PetitionServiceImpl petitionService;
    private final DayScheduleServiceImpl dayScheduleService;

    @GetMapping
    public byte[] getReport(@RequestBody ReportDto dto) {
        return null;
    }
}
