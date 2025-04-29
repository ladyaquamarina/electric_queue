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
    ANOTHER("Другое");  // в ANOTHER входит любая стандартная причина посещения деканата типа подписания документов, зачеток, подача заявлений и прочее

    private final String value;
}
