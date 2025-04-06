package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import queue.models.MailApproveEntity;
import queue.repositories.MailApproveRepository;
import queue.services.MailApproveService;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailApproveServiceImpl implements MailApproveService {
    public static final String MAIL_APPROVE_SUBJECT = "Код подтверждения для электронной очереди";
    public static final String MAIL_APPROVE_TEXT = "%s - ваш код подтверждения";

    private final MailApproveRepository mailApproveRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username:}")
    private String mainMail;

    @Override
    public Mono<Void> sendCodeToMail(String mail, Long chatId, UUID userId) {
        String code = getRandomNumberString();
        SimpleMailMessage mailMessage = createMessage(mail, code);
        MailApproveEntity mailApproveEntity  = createMailApproeEntity(chatId, userId, mail, code);
        return mailApproveRepository.save(mailApproveEntity)
                .map(approveEntity -> {
                    javaMailSender.send(mailMessage);
                    return approveEntity;
                })
                .then(Mono.empty());
    }

    @Override
    public Mono<MailApproveEntity> checkCodeByChatId(Long chatId, String code) {
        return mailApproveRepository.findByChatId(chatId)
                .filter(mailApproveEntity -> Objects.equals(mailApproveEntity.getCode(), code))
                .flatMap(mailApproveEntity -> mailApproveRepository.deleteById(mailApproveEntity.getId())
                        .then(Mono.just(mailApproveEntity)));
    }

    private MailApproveEntity createMailApproeEntity(Long chatId, UUID userId, String mail, String code) {
        MailApproveEntity mailApproveEntity = new MailApproveEntity();
        mailApproveEntity.setId(UUID.randomUUID());
        mailApproveEntity.setChatId(chatId);
        mailApproveEntity.setUserId(userId);
        mailApproveEntity.setMail(mail);
        mailApproveEntity.setCode(code);
        mailApproveEntity.setNew(true);
        return mailApproveEntity;
    }

    private SimpleMailMessage createMessage(String mail, String code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mail);
        simpleMailMessage.setFrom(mainMail);
        simpleMailMessage.setSubject(MAIL_APPROVE_SUBJECT);
        simpleMailMessage.setText(String.format(MAIL_APPROVE_TEXT, code));
        return simpleMailMessage;
    }

    private String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}
