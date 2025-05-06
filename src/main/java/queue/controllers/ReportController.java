package queue.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import queue.dtos.ReportDto;
import queue.services.AuthenticationService;
import queue.services.ReportService;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    private final AuthenticationService authenticationService;

    @GetMapping
    public Mono<byte[]> getReport(
            @RequestParam Long chatId,
            @RequestBody ReportDto dto) {
        return authenticationService.getUserIdByChatId(chatId)
                .flatMap(userId -> reportService.getReport(userId, dto));
    }
}
