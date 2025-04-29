package queue.services;

import queue.dtos.DeputyDeanDto;
import queue.models.DeputyDeanEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeputyDeanService {
    Mono<DeputyDeanEntity> createNewDeputyDean(UUID userId, DeputyDeanDto dto);
    Mono<DeputyDeanEntity> updateDeputyDean(Long chatId, DeputyDeanDto dto);

    Mono<DeputyDeanEntity> getByUserId(UUID userId);
    Flux<DeputyDeanEntity> getAllDeputyDeans();
}
