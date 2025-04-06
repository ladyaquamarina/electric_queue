package queue.services;

import queue.models.MailApproveEntity;
import reactor.core.publisher.Mono;

public interface MailApproveService {
    Mono<Void> sendCodeToMail(String mail);
    Mono<MailApproveEntity> getByChatId(Long chatId);
}
