package queue.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PetitionPurpose {
    ALL("Все"),
    CONSULTATION_WITH_SCIENTIFIC_DIRECTOR("Консультация с научным руководителем"),
    TERM_PAPER_PROTECTION("Защита курсовой работы"),
    FQW_APPROVEMENT("Нормоконтроль"),
    GETTING_DOCUMENTS("Получение направлений или других документов");

    private final String value;
}
