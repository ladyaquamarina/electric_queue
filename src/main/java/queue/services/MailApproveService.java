package queue.services;

import queue.models.MailApproveEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface MailApproveService {
    Mono<Void> sendCodeToMail(String mail, Long chatId, UUID userId);
    Mono<MailApproveEntity> checkCodeByChatId(Long chatId, String code);
}
