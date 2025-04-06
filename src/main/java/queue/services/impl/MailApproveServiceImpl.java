package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import queue.models.MailApproveEntity;
import queue.repositories.MailApproveRepository;
import queue.services.MailApproveService;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MailApproveServiceImpl implements MailApproveService {
    private final MailApproveRepository mailApproveRepository;
    // унаследовать класс для общения с почтой

    @Override
    public Mono<Void> sendCodeToMail(String mail) {
        // сохранить в репо
        // отправить на почту
        return null;
    }

    @Override
    public Mono<MailApproveEntity> getByChatId(Long chatId) {
        return mailApproveRepository.findByChatId(chatId);
    }
}
