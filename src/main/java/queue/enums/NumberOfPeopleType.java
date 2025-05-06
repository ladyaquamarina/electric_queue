package queue.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NumberOfPeopleType {
    FEW("Мало"),
    NORMAL("Приемлемо"),
    MANY("Много");

    private final String value;
}
