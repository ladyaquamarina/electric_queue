package queue.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import queue.models.MailApproveEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface MailApproveRepository extends R2dbcRepository<MailApproveEntity, UUID> {
    Mono<MailApproveEntity> findByChatId(Long chatId);
}
