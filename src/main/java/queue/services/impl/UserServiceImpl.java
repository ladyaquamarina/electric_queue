package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.dtos.UserDto;
import queue.mappers.UserMapper;
import queue.models.UserEntity;
import queue.repositories.UserRepository;
import queue.services.AuthenticationService;
import queue.services.UserService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;

    @Override
    public Mono<UserEntity> createNewUser(UUID userId, UserDto dto) {
        UserEntity entity = createNewEntity(userId, dto);
        return userRepository.save(entity);
    }

    @Override
    public Mono<UserEntity> updateUser(UserDto newUser) {
        return checkNecessityToUpdateUser(newUser)
                .flatMap(check -> userRepository.findById(newUser.getId()))
                .flatMap(oldUser -> userRepository.save(userMapper.update(oldUser, newUser)));
    }

    private UserEntity createNewEntity(UUID userId, UserDto dto) {
        UserEntity entity = userMapper.toEntity(dto);
        entity.setId(userId);
        entity.setNew(true);
        return entity;
    }

    private Mono<Boolean> checkNecessityToUpdateUser(UserDto dto) {
        if (dto.getFirstName() != null || dto.getLastName() != null || dto.getSurName() != null) {
            return Mono.just(false);
        }
        return Mono.empty();
    }
}
