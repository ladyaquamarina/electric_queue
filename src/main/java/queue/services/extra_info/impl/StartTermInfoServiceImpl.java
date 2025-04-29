package queue.services.extra_info.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.models.StartTermInfoEntity;
import queue.repositories.StartTermInfoRepository;
import queue.services.extra_info.StartTermInfoService;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StartTermInfoServiceImpl implements StartTermInfoService {
    private final StartTermInfoRepository startTermInfoRepository;

    @Override
    public Mono<LocalDate> getStartTermInfo(LocalDate today) {
        return startTermInfoRepository.findFirstByStartDay(today)
                .map(StartTermInfoEntity::getStartDay);
    }
}
