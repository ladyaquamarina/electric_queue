package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queue.mappers.DayScheduleMapper;
import queue.mappers.DeputyDeanMapper;
import queue.mappers.PetitionMapper;
import queue.services.DayScheduleService;
import queue.services.DeputyDeanService;
import queue.services.PetitionService;
import queue.services.QueueService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deputy_dean")
public class DeputyDeanController {
    private final DeputyDeanService deputyDeanService;
    private final DayScheduleService dayScheduleService;
    private final PetitionService petitionService;
    private final QueueService queueService;

    private final DeputyDeanMapper deputyDeanMapper;
    private final DayScheduleMapper dayScheduleMapper;
    private final PetitionMapper petitionMapper;
}
