package queue.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@Table("start_term_info")
public class StartTermInfoEntity implements Persistable<LocalDate> {
    @Id
    @Column("start_day")
    private LocalDate startDay;

    @Transient
    private boolean isNew = false;

    public LocalDate getId() {
        return startDay;
    }

    public void setId(LocalDate startDay) {
        this.startDay = startDay;
    }
}
