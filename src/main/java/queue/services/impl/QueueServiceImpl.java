package queue.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;
import queue.dtos.PetitionDto;
import queue.enums.DayTimeType;
import queue.enums.EstimateTimeType;
import queue.enums.NumberOfPeopleType;
import queue.enums.PetitionPurpose;
import queue.enums.TermPartType;
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
import queue.utils.TripleComparator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
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
        return studentService.getByUserId(userId)
                .map(StudentEntity::getId)
                .flatMap(studentId -> dayScheduleService.getById(petitionDto.getDayScheduleId())
                        .filter(daySchedule -> !Objects.equals(daySchedule.getDate(), LocalDate.now()))
                        .flatMap(daySchedule -> petitionService.createPetitionByStudent(studentId, petitionDto)));
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
    public Mono<PetitionEntity> startQueue(UUID userId, UUID dayScheduleId) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMap(deputyDeanId -> dayScheduleService.startDaySchedule(dayScheduleId))
                .flatMap(daySchedule -> petitionService.getFirstWaitingPetition(dayScheduleId));
    }

    @Override
    public Flux<PetitionEntity> endQueue(UUID userId, UUID dayScheduleId) {
        return deputyDeanService.getByUserId(userId)
                .map(DeputyDeanEntity::getId)
                .flatMap(deputyDeanId -> dayScheduleService.endDaySchedule(dayScheduleId))
                .flatMapMany(daySchedule -> petitionService.cancelAllWaitingPetitions(daySchedule.getId()));
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
    public Mono<LinkedHashMap<DayScheduleEntity, NumberOfPeopleType>> predict3BestDayForVisit(PetitionPurpose purpose,
                                                                                               UUID deputyDeanId,
                                                                                               LocalDate start,
                                                                                               LocalDate end) {
        return dayScheduleService.getFromTimeInterval(start, end, deputyDeanId)
                .filter(daySchedule -> daySchedule.getPurpose() == PetitionPurpose.ALL || purpose == daySchedule.getPurpose())
                .map(this::fuzzificate)
                .map(this::aggregate)
                .map(this::defuzzificate)
                .collectList()
                .map(this::get3LeastCrowdedDays);
    }

    private LinkedHashMap<DayScheduleEntity, NumberOfPeopleType> get3LeastCrowdedDays(List<Triple<DayScheduleEntity, NumberOfPeopleType, Integer>> triples) {
        SortedSet<Triple<DayScheduleEntity, NumberOfPeopleType, Integer>> leastCrowdedDays = new TreeSet<>(new TripleComparator()) {};
        triples.forEach(triple -> {
            leastCrowdedDays.add(triple);
            if (leastCrowdedDays.size() > 3) {
                leastCrowdedDays.removeLast();
            }
        });
        LinkedHashMap<DayScheduleEntity, NumberOfPeopleType> result = new LinkedHashMap<>();
        leastCrowdedDays.forEach(setElem -> result.put(setElem.getLeft(), setElem.getMiddle()));
        return result;
    }

    private Triple<DayScheduleEntity, NumberOfPeopleType, Integer> defuzzificate(
            Pair<DayScheduleEntity, Triple<NumberOfPeopleType, Double, Double>> pair) {
        int maxNumberOfPeople = Integer.MAX_VALUE;
        Triple<NumberOfPeopleType, Double, Double> maxProbabilityResult = pair.getValue();

        // сопоставить со значением по графику
        double mu = maxProbabilityResult.getRight();
        switch (maxProbabilityResult.getLeft()) {
            case FEW -> {
                if (mu == 1) {
                    maxNumberOfPeople = 10;
                } else if (mu < 1 && mu > 0) {
                    maxNumberOfPeople = (int) Math.round(-3 * mu + 13);
                }
            }
            case NORMAL -> {
                if (mu == 1) {
                    maxNumberOfPeople = 20;
                } else if (mu < 1 && mu > 0) {
                    maxNumberOfPeople = (int) Math.round(-2 * mu + 22);
                }
            }
            case MANY -> {
                if (mu == 1) {
                    maxNumberOfPeople = 100;
                } else if (mu < 1 && mu > 0) {
                    maxNumberOfPeople = (int) Math.round(mu + 20);
                }
            }
        }

        // вернуть тройку: расписание - словесная_оценка - численая_оценка
        return Triple.of(pair.getKey(),  maxProbabilityResult.getLeft(), maxNumberOfPeople);
    }

    private Pair<DayScheduleEntity, Triple<NumberOfPeopleType, Double, Double>> aggregate(
            Pair<DayScheduleEntity, Triple<Map<DayTimeType, Double>, Map<TermPartType, Double>, Map<EstimateTimeType, Double>>> pair) {
        Map<DayTimeType, Double> dayTimeFuzz = pair.getValue().getLeft();
        Map<TermPartType, Double> termPartFuzz = pair.getValue().getMiddle();
        Map<EstimateTimeType, Double> estimateTimeTypeFuzz = pair.getValue().getRight();
        Pair<NumberOfPeopleType, Double>[] rules = new Pair[5];

        // получить вероятность для каждого правила
        rules[0] = Pair.of(NumberOfPeopleType.MANY, termPartFuzz.get(TermPartType.END));
        rules[1] = Pair.of(NumberOfPeopleType.FEW, Math.min(dayTimeFuzz.get(DayTimeType.MORNING), termPartFuzz.get(TermPartType.MIDDLE)));
        rules[2] = Pair.of(NumberOfPeopleType.NORMAL, Math.min(
                Math.min(estimateTimeTypeFuzz.get(EstimateTimeType.FAST), termPartFuzz.get(TermPartType.MIDDLE)),
                Math.max(dayTimeFuzz.get(DayTimeType.MORNING), dayTimeFuzz.get(DayTimeType.EVENING))));
        rules[3] = Pair.of(NumberOfPeopleType.FEW, Math.min(
                estimateTimeTypeFuzz.get(EstimateTimeType.LONG),
                Math.max(Math.max(termPartFuzz.get(TermPartType.START), termPartFuzz.get(TermPartType.MIDDLE)), termPartFuzz.get(TermPartType.SESSION))));
        rules[4] = Pair.of(NumberOfPeopleType.MANY, Math.min(
                Math.min(estimateTimeTypeFuzz.get(EstimateTimeType.FAST), termPartFuzz.get(TermPartType.SESSION)),
                Math.max(dayTimeFuzz.get(DayTimeType.AFTERNOON), dayTimeFuzz.get(DayTimeType.EVENING))));

        // получить пару с наибольшим значением вероятности
        Triple<NumberOfPeopleType, Double, Double> maxProbabilityResult = null;
        for (Pair<NumberOfPeopleType, Double> rule : rules) {
            if (rule.getValue() != 0) {
                calculateProbability(rule, maxProbabilityResult);
            }
        }
        return Pair.of(pair.getKey(), maxProbabilityResult);
    }

    private void calculateProbability(Pair<NumberOfPeopleType, Double> rule, Triple<NumberOfPeopleType, Double, Double> maxProbabilityResult) {
        // гёдель, мамдани, гоген, лукасевич - альтернативы
        Pair<Double, Double> probabilityByZade = countByZade(rule.getValue());

        if (maxProbabilityResult == null ||
                (maxProbabilityResult.getMiddle() != null &&
                        Math.max(probabilityByZade.getLeft(), maxProbabilityResult.getMiddle()) == probabilityByZade.getLeft())) {
            maxProbabilityResult = Triple.of(rule.getKey(), probabilityByZade.getLeft(), probabilityByZade.getRight());
        }
    }

    // Pair<mu_(Q->P), mu_P>
    private Pair<Double, Double> countByZade(Double pramise) {
        // посылка импликации pramise (Q)
        // заключение импликации (P)
        // формула по Заде: mu_(Q->P) = max(min(mu_Q, mu_P), 1 - mu_Q),
        // где известно только mu_Q

        //  1)  при 1 - mu_Q >= min(mu_Q, mu_P)
        //      пусть mu_Q < mu_P, тогда mu_Q < mu_P <= 1 - mu_Q,
        //          следовательно, mu_Q in [0; 0.5], mu_P in (mu_Q; 1], mu_(Q->P) = 1 - mu_Q
        //      пусть mu_Q >= mu_P, тогда mu_P <= mu_Q <= 1 - mu_Q,
        //          следовательно, mu_Q in (0; 0.5], mu_P in [0; mu_Q], mu_(Q->P) = 1 - mu_Q
        //  2)  при 1 - mu_Q < min(mu_Q, mu_P)
        //      пусть mu_Q < mu_P, тогда 1 - mu_Q < mu_Q < mu_P,
        //          следовательно, mu_Q in (0.5; 1), mu_P in (mu_Q; 1], mu_(Q->P) = mu_Q
        //      пусть mu_Q >= mu_P, тогда 1 - mu_Q < mu_P <= mu_Q,
        //          следовательно, mu_Q in (0.5; 1], mu_P in (0; mu_Q], mu_(Q->P) in (1 - mu_Q; mu_Q]
        //  для ситуаций попадания неизвестной в интервал берем максимальное значение из этого интервала,
        //      сначала определяем mu_(Q->P), потом mu_P,
        //      следовательно, из п.1 mu_P = 1
        //          из п.2 mu_(Q->P) = mu_Q, mu_P = 1

        if (pramise <= 0.5) {
            return Pair.of(1 - pramise, 1.);
        } else {
            return Pair.of(pramise, 1.);
        }
    }

    private Pair<DayScheduleEntity, Triple<Map<DayTimeType, Double>, Map<TermPartType, Double>, Map<EstimateTimeType, Double>>> fuzzificate(
            DayScheduleEntity daySchedule) {
        Map<DayTimeType, Double> dayTime = fuzzificate(daySchedule.getStartAtPlan());
        Map<TermPartType, Double> termPart = fuzzificate(daySchedule.getDate());
        Map<EstimateTimeType, Double> estimateTimeType = fuzzificate(daySchedule.getPurpose());
        return Pair.of(daySchedule, Triple.of(dayTime, termPart, estimateTimeType));
    }

    private Map<DayTimeType, Double> fuzzificate(LocalTime startAtPlan) {
        double x = startAtPlan.getHour() + startAtPlan.getMinute() / 60.;
        double morningAccessoryFunc = ((x >= 8 && x <= 11.5) ? 1 : 0) +
                ((x > 11.5 && x < 12.5) ? (-x + 12.5) : 0);
        double afternoonAccessoryFunc = ((x > 12 && x < 13) ? (x - 12) : 0) +
                ((x >= 13 && x <= 16) ? 1 : 0) +
                ((x > 16 && x < 17) ? (-x + 17) : 0);
        double eveningAccessoryFunc = ((x > 16.5 && x < 17.5) ? (x - 16.5) : 0) +
                ((x >= 17.5 && x < 21) ? 1 : 0);
        return Map.of(
                DayTimeType.MORNING, morningAccessoryFunc,
                DayTimeType.AFTERNOON, afternoonAccessoryFunc,
                DayTimeType.EVENING, eveningAccessoryFunc
        );
    }

    private Map<TermPartType, Double> fuzzificate(LocalDate date) {
        int y = getWeekNumber(date);
        double startAccessoryFunc = ((y >= 1 && y <= 4) ? 1 : 0) +
                ((y > 4 && y < 6) ? (-0.5 * y + 3) : 0);
        double middleAccessoryFunc = ((y > 5 && y < 7) ? (0.5 * y - 2.5) : 0) +
                ((y >= 7 && y <= 11) ? 1 : 0) +
                ((y > 11 && y < 13) ? (-0.5 * y + 6.5) : 0);
        double endAccessoryFunc = ((y > 12 && y < 14) ? (0.5 * y - 6) : 0) +
                ((y >= 14 && y <= 16) ? 1 : 0) +
                ((y > 16 && y < 17) ? (-y + 17) : 0);
        double sessionAccessoryFunc = ((y > 16 && y < 17) ? (y - 16) : 0) +
                ((y >= 17) ? 1 : 0);
        return Map.of(
                TermPartType.START, startAccessoryFunc,
                TermPartType.MIDDLE, middleAccessoryFunc,
                TermPartType.END, endAccessoryFunc,
                TermPartType.SESSION, sessionAccessoryFunc
        );
    }

    private Map<EstimateTimeType, Double> fuzzificate(PetitionPurpose purpose) {
        return Map.of(
                EstimateTimeType.FAST, isLongTime(purpose) ? 0. : 1.,
                EstimateTimeType.LONG, isLongTime(purpose) ? 1. : 0.
        );
    }

    private boolean isLongTime(PetitionPurpose purpose) {
        return purpose != PetitionPurpose.ALL && purpose != PetitionPurpose.ANOTHER;
    }

    private int getWeekNumber(LocalDate date) {
        int currentTermNumber = getTermNumber(date.getMonth());
        LocalDate startTerm = startTermInfoService.getStartTermInfo(date)
                .blockOptional()
                .orElse(LocalDate.MIN);
        if (startTerm == LocalDate.MIN || getTermNumber(startTerm.getMonth()) != currentTermNumber) {
            startTerm = getFirstWeekdaySinceThatDate(currentTermNumber == 1 ?
                    LocalDate.of(
                            (date.getMonth() == JANUARY ? date.getYear() - 1 : date.getYear()),
                            SEPTEMBER,
                            1) :
                    LocalDate.of(date.getYear(), FEBRUARY, 7));
        }
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int currentWeekNumber = date.get(weekOfYear) +
                (date.getMonth() == JANUARY ? LocalDate.of(date.getYear() - 1, DECEMBER, 31).get(weekOfYear) : 0);
        return currentWeekNumber - startTerm.get(weekOfYear);
    }

    // осенний семестр - первый, весенний - второй
    private int getTermNumber(Month month) {
        return SPRING_MONTHS.contains(month) ? 2 : 1;
    }

    private LocalDate getFirstWeekdaySinceThatDate(LocalDate date) {
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() != DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }
        return date;
    }
}
