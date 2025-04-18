package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import queue.models.UserEntity;
import queue.services.AuthenticationService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/mail")
    public Mono<Void> sendMail(@RequestParam String mail,
                               @RequestParam Long chatId) {
        return authenticationService.sendMail(mail, chatId);
    }

//    @PostMapping("/mail")
//    public Mono<UUID> getMail(@RequestBody String mail) {
//        return authenticationService.createNewUser(mail)
//                .map(UserEntity::getId);
//    }

    // возвращает userId
    @PostMapping("/code")
    public Mono<UUID> checkCode(@RequestParam("chatId") Long chatId,
                                @RequestParam("code") String code) {
        return authenticationService.checkCode(chatId, code);
    }
}
