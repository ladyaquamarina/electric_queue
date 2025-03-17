package queue.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import queue.enums.PetitionPurpose;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("day_schedule")
public class DayScheduleEntity implements Persistable<UUID> {
    @Column("id")
    private UUID id;
    @Column("deputy_dean_id")
    private UUID deputyDeanId;
    @Column("date")
    private LocalDateTime date;
    @Column("start_at_plan")
    private LocalDateTime startAtPlan;
    @Column("end_at_plan")
    private LocalDateTime endAtPlan;
    @Column("number_of_student_plan")
    private Integer numberOfStudentPlan;
    @Column("start_at_fact")
    private LocalDateTime startAtFact;
    @Column("endAtFact")
    private LocalDateTime endAtFact;
    @Column("number_of_student_fact")
    private Integer numberOfStudentFact;
    @Column("purpose")
    private PetitionPurpose purpose;

    @Transient
    private boolean isNew = false;
    @Transient
    private DeputyDeanEntity deputyDean;
}
