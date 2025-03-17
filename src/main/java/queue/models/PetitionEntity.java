package queue.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import queue.enums.PetitionPurpose;
import queue.enums.PetitionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("petition")
public class PetitionEntity implements Persistable<UUID> {
    @Column("id")
    private UUID id;
    @Column("student_id")
    private UUID studentId;
    @Column("deputy_dean_id")
    private UUID deputyDeanId;
    @Column("day_schedule_id")
    private UUID dayScheduleId;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("started_at")
    private LocalDateTime startedAt;
    @Column("ended_at")
    private LocalDateTime endedAt;
    @Column("purpose")
    private PetitionPurpose purpose;
    @Column("status")
    private PetitionStatus status;

    @Transient
    private boolean isNew = false;
    @Transient
    private StudentEntity student;
    @Transient
    private DeputyDeanEntity deputyDean;
    @Transient
    private DayScheduleEntity daySchedule;
}
