package queue.services;

import queue.dtos.ReportDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReportService {
    Mono<byte[]> getReport(UUID userId, ReportDto dto);
}
