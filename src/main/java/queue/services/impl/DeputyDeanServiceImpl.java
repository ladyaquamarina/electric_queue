package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.dtos.DeputyDeanDto;
import queue.dtos.UserDto;
import queue.mappers.DeputyDeanMapper;
import queue.models.DeputyDeanEntity;
import queue.models.UserEntity;
import queue.repositories.DeputyDeanRepository;
import queue.services.DeputyDeanService;
import queue.services.UserService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeputyDeanServiceImpl implements DeputyDeanService {
    private final DeputyDeanRepository deputyDeanRepository;

    private final UserService userService;
    private final DeputyDeanMapper deputyDeanMapper;

    @Override
    public Mono<DeputyDeanEntity> createNewDeputyDean(Long chatId, DeputyDeanDto dto) {
        return userService.createNewUser(chatId, dto.getUser())
                .map(user -> dtoToEntity(dto, user))
                .flatMap(deputyDean -> deputyDeanRepository.findByUserId(deputyDean.getUserId())
                        .switchIfEmpty(deputyDeanRepository.save(deputyDean)));
    }

    @Override
    public Mono<DeputyDeanEntity> updateDeputyDean(Long chatId, DeputyDeanDto dto) {
        return checkNecessityToUpdateUser(dto.getUser())
                .flatMap(check -> userService.updateUser(dto.getUser()))
                .then(deputyDeanRepository.findById(dto.getId()))
                .flatMap(oldDeputyDean -> updateDeputyDeanEntity(dto, oldDeputyDean)
                        .flatMap(deputyDeanRepository::save)
                        .switchIfEmpty(Mono.just(oldDeputyDean)));
    }

    @Override
    public Mono<DeputyDeanEntity> getByUserId(UUID userId) {
        return deputyDeanRepository.findByUserId(userId);
    }

    private Mono<DeputyDeanEntity> updateDeputyDeanEntity(DeputyDeanDto newDto, DeputyDeanEntity oldEntity) {
        if (newDto.getCourse() != null || newDto.getFaculty() != null) {
            return Mono.just(deputyDeanMapper.update(oldEntity, newDto));
        }
        return Mono.empty();
    }

    private Mono<Boolean> checkNecessityToUpdateUser(UserDto dto) {
        if (dto.getFirstName() == null && dto.getLastName() == null && dto.getSurName() == null) {
            return Mono.just(false);
        }
        return Mono.empty();
    }

    private DeputyDeanEntity dtoToEntity(DeputyDeanDto dto, UserEntity user) {
        DeputyDeanEntity entity = deputyDeanMapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        entity.setUser(user);
        entity.setNew(true);
        return entity;
    }
}
