package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.dtos.DeputyDeanDto;
import queue.models.DeputyDeanEntity;
import queue.repositories.DeputyDeanRepository;
import queue.services.DeputyDeanService;
import queue.services.UserService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeputyDeanServiceImpl implements DeputyDeanService {
    private final DeputyDeanRepository deputyDeanRepository;
    private final UserService userService;

    @Override
    public Mono<DeputyDeanEntity> createNewDeputyDean(DeputyDeanDto dto) {
        return null;
    }

    @Override
    public Mono<DeputyDeanEntity> updateDeputyDean(UUID deputyDeanId, DeputyDeanDto dto) {
        return null;
    }
}
