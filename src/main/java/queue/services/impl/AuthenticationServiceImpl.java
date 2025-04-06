package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.models.AuthenticationInfoEntity;
import queue.models.UserEntity;
import queue.repositories.AuthenticationInfoRepository;
import queue.services.AuthenticationService;
import queue.services.MailApproveService;
import queue.services.UserAuthenticationService;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static queue.utils.Utils.ZERO_UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationInfoRepository authenticationInfoRepository;
    private final UserAuthenticationService userAuthenticationService;
    private final MailApproveService mailApproveService;

    @Override
    public Mono<UUID> getUserIdByChatId(Long chatId) {
        return authenticationInfoRepository.findByChatId(chatId)
                .map(AuthenticationInfoEntity::getUserId);
    }

    @Override
    public Mono<Void> sendMail(String mail) {
        return getUserIdByMail(mail)
                .flatMap(userId -> mailApproveService.sendCodeToMail(mail));
    }

    @Override
    public Mono<AuthenticationInfoEntity> updateChatId(UUID id, Long chatId) {
        return authenticationInfoRepository.save(getEntity(id, chatId));
    }

    @Override
    public Mono<Long> checkCode(Long chatId, String code) {
        return mailApproveService.getByChatId();
    }

    @Override
    public Mono<UserEntity> createNewUser(String mail) {
        return null;
    }

    private Mono<UUID> getUserIdByMail(String mail) {
        return userAuthenticationService.getByMail(mail)
                .map(UserEntity::getId)
                .switchIfEmpty(Mono.just(ZERO_UUID));
    }

//    private AuthenticationInfoEntity getEntity(UUID id, Long chatId) {
//        return new AuthenticationInfoEntity(id, chatId, false);
//    }
}
