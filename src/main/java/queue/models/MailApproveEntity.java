package queue.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("mail_approve")
public class MailApproveEntity implements Persistable<Long> {
    @Id
    @Column("id")
    private UUID id;
    @Column("chatId")
    private Long chatId;
    @Column("code")
    private Integer code;

    @Transient
    private boolean isNew = false;
}
