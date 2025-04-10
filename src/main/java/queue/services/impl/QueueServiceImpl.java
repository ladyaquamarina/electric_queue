package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import queue.dtos.PetitionDto;
import queue.enums.PetitionStatus;
import queue.models.PetitionEntity;
import queue.models.StudentEntity;
import queue.services.QueueService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    @Override
    public Mono<PetitionEntity> addToQueue(PetitionEntity petition, String faculty, UUID deputyDeanId) {
        return null;
    }

    @Override
    public Mono<PetitionEntity> removeFromQueue(PetitionEntity petition) {
        return null;
    }

    @Override
    public Mono<PetitionEntity> getNextPetition(UUID dayScheduleId) {
        return null;
    }
}
