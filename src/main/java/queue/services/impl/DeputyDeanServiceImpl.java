package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.repositories.DeputyDeanRepository;
import queue.services.DeputyDeanService;
import queue.services.UserService;

@Service
@RequiredArgsConstructor
public class DeputyDeanServiceImpl implements DeputyDeanService {
    private final DeputyDeanRepository deputyDeanRepository;
    private final UserService userService;
}
