package queue.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PetitionStatus {
    WAITING("В ожидании обработки"),
    IN_PROCESSING("В обработке"),
    PROCESSED("Обработано"),
    CANCELLED("Отменено");

    private final String value;
}
