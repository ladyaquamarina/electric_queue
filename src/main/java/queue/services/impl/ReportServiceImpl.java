package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import queue.dtos.ReportDto;
import queue.models.PetitionEntity;
import queue.services.DayScheduleService;
import queue.services.DeputyDeanService;
import queue.services.PetitionService;
import queue.services.ReportService;
import queue.services.StudentService;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private static final LocalDate defaultEndDate = LocalDate.now().minusDays(1);
    private static final LocalDate defaultStartDate = LocalDate.now().minusYears(1);

    private final DayScheduleService dayScheduleService;
    private final StudentService studentService;
    private final DeputyDeanService deputyDeanService;
    private final PetitionService petitionService;

    @Override
    public Mono<byte[]> getReport(UUID userId, ReportDto dto) {
        return Mono.just(validateDate(getDefaultStartDate(dto.getStartOfPeriod()), getDefaultEndDate(dto.getEndOfPeriod())))
                .flatMapMany(dates -> petitionService.getAllByStartDateAndEndDate(
                        dates.getLeft(), dates.getRight(), dto.getDeputyDeanId(), dto.getPurpose()))
                .collectList()
                .flatMap(this::fillStudent)
                .map(petitions -> filterByStudentParams(dto, petitions))
                .flatMap(this::fillDeputyDean)
                .flatMap(this::fillDaySchedule)
                .map(petitions -> toFile(petitions, dto));
    }

    private byte[] toFile(List<PetitionEntity> petitions, ReportDto dto) {
        return null;
    }

    private List<PetitionEntity> filterByStudentParams(ReportDto dto, List<PetitionEntity> petitions) {
        if (dto.getFaculty() != null) {
             petitions = petitions.stream()
                    .filter(petition -> petition.getStudent() != null && Objects.equals(petition.getStudent().getFaculty(), dto.getFaculty()))
                    .toList();
        }
        if (dto.getGroup() != null) {
            petitions = petitions.stream()
                    .filter(petition -> petition.getStudent() != null && Objects.equals(petition.getStudent().getGroup(), dto.getGroup()))
                    .toList();
        }
        if (dto.getCourse() != null) {
            petitions = petitions.stream()
                    .filter(petition -> petition.getStudent() != null && Objects.equals(petition.getStudent().getCourse(), dto.getCourse()))
                    .toList();
        }
        return petitions;
    }

    private Mono<List<PetitionEntity>> fillDaySchedule(List<PetitionEntity> petitions) {
        return dayScheduleService.getByDayScheduleIds(petitions.stream().map(PetitionEntity::getDayScheduleId).toList())
                .map(daySchedule -> {
                    for (PetitionEntity petition : petitions) {
                        if (petition.getDayScheduleId() == daySchedule.getId()) {
                            petition.setDaySchedule(daySchedule);
                            break;
                        }
                    }
                    return daySchedule;
                })
                .collectList()
                .map(students -> petitions);
    }

    private Mono<List<PetitionEntity>> fillDeputyDean(List<PetitionEntity> petitions) {
        return deputyDeanService.getByDeputyDeanIds(petitions.stream().map(PetitionEntity::getDeputyDeanId).toList())
                .map(deputyDean -> {
                    for (PetitionEntity petition : petitions) {
                        if (petition.getDeputyDeanId() == deputyDean.getId()) {
                            petition.setDeputyDean(deputyDean);
                            break;
                        }
                    }
                    return deputyDean;
                })
                .collectList()
                .map(students -> petitions);
    }

    private Mono<List<PetitionEntity>> fillStudent(List<PetitionEntity> petitions) {
        return studentService.getByStudentIds(petitions.stream().map(PetitionEntity::getStudentId).toList())
                .map(student -> {
                    for (PetitionEntity petition : petitions) {
                        if (petition.getStudentId() == student.getId()) {
                            petition.setStudent(student);
                            break;
                        }
                    }
                    return student;
                })
                .collectList()
                .map(students -> petitions);
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
