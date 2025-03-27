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
    public Mono<UserEntity> createNewUser(Long chatId, UserDto dto) {
        String mail = dto.getMail();
        UserEntity entity = createNewEntity(chatId, dto);
        return userRepository.findByMail(mail)
                .flatMap(user -> authenticationService.updateChatId(user.getAuthenticationInfoId(), chatId)
                        .map(auth -> user))
                .switchIfEmpty(userRepository.save(entity));
    }

    @Override
    public Mono<UserEntity> updateUser(UserDto newUser) {
        return userRepository.findById(newUser.getId())
                .flatMap(oldUser -> userRepository.save(userMapper.update(oldUser, newUser)));
    }

    @Override
    public Mono<UserEntity> getByChatId(Long chatId) {
        return authenticationService.getByChatId(chatId)
                .flatMap(auth -> userRepository.findByAuthenticationInfoId(auth.getId()));
    }

    private UserEntity createNewEntity(Long chatId, UserDto dto) {
        UserEntity entity = userMapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        entity.setNew(true);
        return entity;
    }
}
