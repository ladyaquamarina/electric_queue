package queue.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetitionDto {
    private UUID id;
    private UUID studentId;
    private UUID deputyDeanId;
    private UUID dayScheduleId;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private NameValueDto purpose;
    private NameValueDto status;

    private StudentDto student;
    private DeputyDeanDto deputyDean;
    private DayScheduleDto daySchedule;
}
