package queue.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.repositories.PetitionRepository;

@Service
@RequiredArgsConstructor
public class PetitionService {
    private final PetitionRepository petitionRepository;
    private final QueueService queueService;
}
