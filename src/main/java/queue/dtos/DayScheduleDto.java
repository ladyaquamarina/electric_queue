package queue.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayScheduleDto {
    private UUID id;
    private UUID deputyDeanId;
    private LocalDateTime date;
    private LocalDateTime startAtPlan;
    private LocalDateTime endAtPlan;
    private Integer numberOfStudentPlan;
    private LocalDateTime startAtFact;
    private LocalDateTime endAtFact;
    private Integer numberOfStudentFact;
    private NameValueDto purpose;

    private DeputyDeanDto deputyDean;
}
