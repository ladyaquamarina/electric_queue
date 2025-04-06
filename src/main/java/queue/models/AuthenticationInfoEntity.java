package queue.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("authentication_info")
public class AuthenticationInfoEntity implements Persistable<UUID> {
    @Id
    @Column("userId")
    private UUID userId;    // соответствует user.id
    @Column("chat_id")
    private Long chatId;
    @Column("mail")
    private String mail;

    @Transient
    private boolean isNew = false;

    public UUID getId()  {
        return userId;
    }

    public UUID setId(UUID id) {
        this.userId = id;
        return this.userId;
    }
}
