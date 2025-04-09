package queue.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PetitionStatus {
    WAITING("В ожидании обработки"),
    IN_PROCESSING("В обработке"),
    PROCESSED("Обработано"),
    CANCELLED("Отменено"),
    NOT_PROCESSED("Не обработано");

    private final String value;
}
