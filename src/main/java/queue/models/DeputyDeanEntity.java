package queue.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("deputy_dean")
public class DeputyDeanEntity implements Persistable<UUID> {
    @Column("id")
    private UUID id;
    @Column("user_id")
    private UUID userId;
    @Column("faculty")
    private String faculty;
    @Column("course")
    private Integer course;

    @Transient
    private boolean isNew = false;
    @Transient
    private UserEntity user;
}
