package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.models.UserEntity;
import queue.repositories.UserRepository;
import queue.services.UserAuthenticationService;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private final UserRepository userRepository;

    @Override
    public Mono<UserEntity> getByMail(String mail) {
        return userRepository.findByMail(mail);
    }
}
