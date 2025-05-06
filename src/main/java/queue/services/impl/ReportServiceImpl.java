package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import queue.dtos.ReportDto;
import queue.services.DayScheduleService;
import queue.services.PetitionService;
import queue.services.ReportService;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private static final LocalDate defaultEndDate = LocalDate.now().minusDays(1);
    private static final LocalDate defaultStartDate = LocalDate.now().minusYears(1);

    private final DayScheduleService dayScheduleService;
    private final PetitionService petitionService;

    @Override
    public Mono<byte[]> getReport(UUID userId, ReportDto dto) {
        return Mono.just(validateDate(getDefaultStartDate(dto.getStartOfPeriod()), getDefaultEndDate(dto.getEndOfPeriod())))
                .flatMapMany(dates -> petitionService.getAllByStartDateAndEndDate(dates.getLeft(), dates.getRight()))
                .flatMap();
    }

    private Pair<LocalDate, LocalDate> validateDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.toEpochDay() > LocalDate.now().toEpochDay()) {
            return Pair.of(defaultStartDate, defaultEndDate);
        } else if (endDate.toEpochDay() > LocalDate.now().toEpochDay()) {
            return Pair.of(startDate, defaultEndDate);
        } else if (endDate.toEpochDay() < startDate.toEpochDay()) {
            return Pair.of(endDate, startDate);
        } else {
            return Pair.of(startDate, endDate);
        }
    }

    private LocalDate getDefaultEndDate(LocalDate endOfPeriod) {
        return endOfPeriod == null ? defaultEndDate : endOfPeriod;
    }

    private LocalDate getDefaultStartDate(LocalDate startOfPeriod) {
        return startOfPeriod == null ? defaultStartDate : startOfPeriod;
    }
}
