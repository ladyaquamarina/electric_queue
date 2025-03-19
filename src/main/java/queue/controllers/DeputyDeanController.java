package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queue.mappers.DayScheduleMapper;
import queue.mappers.DeputyDeanMapper;
import queue.mappers.PetitionMapper;
import queue.services.impl.DayScheduleServiceImpl;
import queue.services.impl.DeputyDeanServiceImpl;
import queue.services.impl.PetitionServiceImpl;
import queue.services.impl.QueueServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deputy_dean")
public class DeputyDeanController {
    private final DeputyDeanServiceImpl deputyDeanService;
    private final DayScheduleServiceImpl dayScheduleService;
    private final PetitionServiceImpl petitionService;
    private final QueueServiceImpl queueService;

    private final DeputyDeanMapper deputyDeanMapper;
    private final DayScheduleMapper dayScheduleMapper;
    private final PetitionMapper petitionMapper;
}
