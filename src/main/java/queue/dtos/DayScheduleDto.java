package queue.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayScheduleDto {
    private UUID id;
    private UUID deputyDeanId;
    private LocalDate date;
    private LocalTime startAtPlan;
    private LocalTime endAtPlan;
    private Integer numberOfStudentPlan;
    private LocalTime startAtFact;
    private LocalTime endAtFact;
    private Integer numberOfStudentFact;
    private NameValueDto purpose;

    private DeputyDeanDto deputyDean;
}
