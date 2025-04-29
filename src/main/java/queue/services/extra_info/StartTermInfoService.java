package queue.services.extra_info;

import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface StartTermInfoService {
    Mono<LocalDate> getStartTermInfo(LocalDate today);
}
