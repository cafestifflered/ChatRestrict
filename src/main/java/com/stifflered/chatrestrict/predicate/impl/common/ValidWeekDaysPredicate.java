package com.stifflered.chatrestrict.predicate.impl.common;

import com.stifflered.chatrestrict.predicate.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

import java.time.*;
import java.util.*;

public record ValidWeekDaysPredicate(Set<DayOfWeek> days) implements UserInputPredicate {

    @Override
    public boolean get(String input, Player sender) {
        return days.contains(DayOfWeek.from(OffsetDateTime.now()));
    }

    public static class ValidWeekDaysPredicateDeserializer implements PredicateDeserializer<ValidWeekDaysPredicate> {

        @Override
        public ValidWeekDaysPredicate deserialize(ConfigurationSection section) {
            List<String> weekDays = section.getStringList("week_days");
            Set<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
            for (String weekDay : weekDays) {
                days.add(DayOfWeek.valueOf(weekDay.toUpperCase()));
            }

            return new ValidWeekDaysPredicate(days);
        }
    }
}
