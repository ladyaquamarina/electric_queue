package queue.utils;

import org.apache.commons.lang3.tuple.Triple;
import queue.enums.NumberOfPeopleType;
import queue.models.DayScheduleEntity;

import java.util.Comparator;

public class TripleComparator implements Comparator<Triple<DayScheduleEntity, NumberOfPeopleType, Integer>> {
    @Override
    public int compare(Triple<DayScheduleEntity, NumberOfPeopleType, Integer> o1,
                       Triple<DayScheduleEntity, NumberOfPeopleType, Integer> o2) {
        return o1.getRight().compareTo(o2.getRight());
    }
}
