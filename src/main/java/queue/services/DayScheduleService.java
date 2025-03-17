package queue.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.repositories.DayScheduleRepository;

@Service
@RequiredArgsConstructor
public class DayScheduleService {
    private final DayScheduleRepository dayScheduleRepository;
}
