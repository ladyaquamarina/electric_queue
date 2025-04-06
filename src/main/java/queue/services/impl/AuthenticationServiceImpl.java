package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.models.AuthenticationInfoEntity;
import queue.models.MailApproveEntity;
import queue.repositories.AuthenticationInfoRepository;
import queue.services.AuthenticationService;
import queue.services.MailApproveService;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import static queue.utils.Utils.ZERO_UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationInfoRepository authenticationInfoRepository;
    private final MailApproveService mailApproveService;

    @Override
    public Mono<UUID> getUserIdByChatId(Long chatId) {
        return authenticationInfoRepository.findByChatId(chatId)
                .map(AuthenticationInfoEntity::getUserId);
    }

    @Override
    public Mono<Void> sendMail(String mail, Long chatId) {
        return authenticationInfoRepository.findByMail(mail)
                .map(AuthenticationInfoEntity::getUserId)
                .switchIfEmpty(Mono.just(ZERO_UUID))
                .flatMap(userId -> mailApproveService.sendCodeToMail(mail, chatId, userId));
    }

    @Override
    public Mono<UUID> checkCode(Long chatId, String code) {
        return mailApproveService.checkCodeByChatId(chatId, code)
                .flatMap(this::saveNewChatId);
    }

    private Mono<UUID> saveNewChatId(MailApproveEntity mailApproveEntity) {
        AuthenticationInfoEntity authenticationInfo = getAuthenticationInfoEntity(
                mailApproveEntity.getUserId(), mailApproveEntity.getChatId(), mailApproveEntity.getMail());
        return authenticationInfoRepository.save(authenticationInfo)
                .map(AuthenticationInfoEntity::getUserId);
    }

    private AuthenticationInfoEntity getAuthenticationInfoEntity(UUID userId, Long chatId, String mail) {
        AuthenticationInfoEntity entity = new AuthenticationInfoEntity();
        entity.setChatId(chatId);
        entity.setMail(mail);
        if (Objects.equals(userId, ZERO_UUID))  {
            entity.setUserId(UUID.randomUUID());
            entity.setNew(true);
        } else {
            entity.setUserId(userId);
            entity.setNew(false);
        }
        return entity;
    }
}
