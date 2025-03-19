package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.dtos.UserDto;
import queue.mappers.UserMapper;
import queue.models.UserEntity;
import queue.repositories.UserRepository;
import queue.services.UserService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<UserEntity> createNewUser(UserDto dto) {
        UserEntity entity = createNewEntity(dto);
        return userRepository.save(entity);
    }

    @Override
    public Mono<UserEntity> updateUser(UserDto newUser) {
        return userRepository.findById(newUser.getId())
                .flatMap(oldUser -> userRepository.save(userMapper.update(oldUser, newUser)));
    }

    private UserEntity createNewEntity(UserDto dto) {
        UserEntity entity = userMapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        entity.setNew(true);
        return entity;
    }
}
