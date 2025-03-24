package queue.services;

import queue.dtos.DeputyDeanDto;
import queue.models.DeputyDeanEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeputyDeanService {
    Mono<DeputyDeanEntity> createNewDeputyDean(DeputyDeanDto dto);

    Mono<DeputyDeanEntity> updateDeputyDean(UUID deputyDeanId, DeputyDeanDto dto);
}
