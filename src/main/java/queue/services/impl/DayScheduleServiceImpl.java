package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.models.DayScheduleEntity;
import queue.repositories.DayScheduleRepository;
import queue.services.DayScheduleService;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DayScheduleServiceImpl implements DayScheduleService {
    private final DayScheduleRepository dayScheduleRepository;

    @Override
    public Flux<DayScheduleEntity> getDaySchedules(UUID userId, UUID deputyDeanId) {
        return null;
    }
}
