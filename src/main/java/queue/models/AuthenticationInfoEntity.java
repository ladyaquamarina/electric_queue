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
    @Column("id")
    private UUID id;
    @Column("chat_id")
    private Long chatId;

    @Transient
    private boolean isNew = false;
}
