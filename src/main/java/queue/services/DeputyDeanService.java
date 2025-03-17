package queue.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.repositories.DeputyDeanRepository;
import queue.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class DeputyDeanService {
    private final UserRepository userRepository;
    private final DeputyDeanRepository deputyDeanRepository;
}
