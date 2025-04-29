package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;
import queue.dtos.PetitionDto;
import queue.enums.PetitionPurpose;
import queue.models.DayScheduleEntity;
import queue.models.DeputyDeanEntity;
import queue.models.PetitionEntity;
import queue.models.StudentEntity;
import queue.services.DayScheduleService;
import queue.services.DeputyDeanService;
import queue.services.PetitionService;
import queue.services.QueueService;
import queue.services.StudentService;
import queue.services.extra_info.StartTermInfoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static java.time.Month.APRIL;
import static java.time.Month.AUGUST;
import static java.time.Month.DECEMBER;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.JUNE;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {
    private static final Set<Month> SPRING_MONTHS = Set.of(FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST);
    private static final Set<Month> AUTUMN_MONTHS = Set.of(SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER, JANUARY);


    private final PetitionService petitionService;
    private final StudentService studentService;
    private final DeputyDeanService deputyDeanService;
    private final DayScheduleService dayScheduleService;

    private final StartTermInfoService startTermInfoService;

    @Override
    public Mono<PetitionEntity> addToQueue(UUID userId, PetitionDto petitionDto) {
        // postgresql партиции https://habr.com/ru/articles/273933/
        return studentService.getByUserId(userId)
                .map(StudentEntity::getId)
                .flatMap(studentId -> petitionService.createPetitionByStudent(studentId, petitionDto));
    }

    @Override
    public Mono<PetitionEntity> removeFromQueue(UUID userId, UUID petitionId) {
        return studentService.getByUserId(userId)
                .map(StudentEntity::getId)
                .flatMap(studentId -> petitionService.cancelPetition(petitionId));
    }

    @Override
    public Mono<Long> getNumberInQueue(UUID userId, UUID dayScheduleId) {
        return studentService.getByUserId(userId)
                .map(StudentEntity::getId)
                .flatMapMany(studentId -> petitionService.getWaitingStudentInQueueBeforeThisStudent(studentId, dayScheduleId))
                .count();
    }

    @Override
    public Mono<PetitionEntity> completeAndGetNextPetition(UUID userId, UUID dayScheduleId) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMap(deputyDeanId -> petitionService.completePetition(dayScheduleId))
                .map(PetitionEntity::getCreatedAt)
                .flatMap(createdAt  -> petitionService.getFirstWaitingPetitionAfterTimestamp(createdAt, dayScheduleId));
    }

    @Override
    public Mono<PetitionEntity> addPetitionBypassingQueue(UUID userId, PetitionDto petitionDto) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMap(deputyDeanId -> petitionService.createPetitionByDeputyDean(deputyDeanId, petitionDto));
    }

    @Override
    public Flux<DayScheduleService> predict3BestDayForVisit(PetitionPurpose purpose,
                                                            UUID deputyDeanId,
                                                            LocalDate start,
                                                            LocalDate end) {
        return dayScheduleService.getFromTimeInterval(start, end, deputyDeanId)
                .filter(daySchedule ->
                        daySchedule.getPurpose() == PetitionPurpose.ALL || purpose == daySchedule.getPurpose())
                .map(daySchedule -> fuzzificate(daySchedule))
                .collectList()
                .map(list -> );
    }

    // возвращает тройку <x, y, z>,  где
    //  x - фаззификация по времени суток
    //  y - фаззификация по периоду семестра
    //  z - фаззификация по предполагаемому среднему времени посещения
    private Triple<Double, Double, Double> fuzzificate(DayScheduleEntity daySchedule) {
        double x = fuzzificate(daySchedule.getStartAtPlan());
        double y = fuzzificate(daySchedule.getDate());
        double z = fuzzificate(daySchedule.getPurpose());
        return Triple.of(x, y, z);
    }

    private double fuzzificate(LocalTime startAtPlan) {
        int x = startAtPlan.getHour();
        double morningAccessoryFunc = ((x >= 8 && x < 11.5) ? 1 : 0) +
                ((x >= 11.5 && x < 12.5) ? (-1 * x + 12.5) : 0);
        double dayAccessoryFunc = ((x >= 12 && x < 13) ? (x - 12) : 0) +
                ((x >= 13 && x < 16) ? 1 : 0) +
                ((x >= 16 && x < 17) ? (-1 * x + 17) : 0);
        double eveningAccessoryFunc = ((x >= 16.5 && x < 17) ? (x - 16.5) : 0) +
                ((x >= 17.5 && x < 21) ? 1 : 0);
        return Math.max(Math.max(morningAccessoryFunc, dayAccessoryFunc), eveningAccessoryFunc);
    }

    private double fuzzificate(LocalDate date) {
        int y = getWeekNumber(date);
        double startAccessoryFunc = ;
        double middleAccessoryFunc = ;
        double endAccessoryFunc = ;
        double sessionAccessoryFunc = ;
        return Math.max(Math.max(startAccessoryFunc, middleAccessoryFunc), Math.max(endAccessoryFunc, sessionAccessoryFunc));
    }

    private double fuzzificate(PetitionPurpose purpose) {
    }

    private int getWeekNumber(LocalDate date) {
        int currentTermNumber = getTermNumber(date.getMonth());
        LocalDate startTerm = startTermInfoService.getStartTermInfo(date)
                .blockOptional()
                .orElse(LocalDate.MIN);
        if (startTerm == LocalDate.MIN || getTermNumber(startTerm.getMonth()) != currentTermNumber) {
            startTerm = currentTermNumber == 1 ?
                    LocalDate.of(
                            (date.getMonth() == JANUARY ? date.getYear() - 1 : date.getYear()),
                            SEPTEMBER,
                            1) :
                    LocalDate.of(date.getYear(), FEBRUARY, 7);
        }
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int currentWeekNumber = date.get(woy) +
                (date.getMonth() == JANUARY ? LocalDate.of(date.getYear() - 1, DECEMBER, 31).get(woy) : 0);
        return currentWeekNumber - startTerm.get(woy);
    }

    // осенний семестр - первый, весенний - второй
    private int getTermNumber(Month month) {
        return SPRING_MONTHS.contains(month) ? 2 : 1;
    }
}
