package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.repositories.DayScheduleRepository;
import queue.services.DayScheduleService;

@Service
@RequiredArgsConstructor
public class DayScheduleServiceImpl implements DayScheduleService {
    private final DayScheduleRepository dayScheduleRepository;
}
