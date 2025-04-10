package queue.services;

import queue.models.PetitionEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface QueueService {
    Mono<PetitionEntity> addToQueue(PetitionEntity petition, String faculty, UUID deputyDeanId);
    Mono<PetitionEntity> removeFromQueue(PetitionEntity petition);
    Mono<PetitionEntity> getNextPetition(UUID dayScheduleId);
}
