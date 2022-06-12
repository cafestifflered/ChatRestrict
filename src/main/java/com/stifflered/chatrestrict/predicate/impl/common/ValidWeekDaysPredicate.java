package com.stifflered.chatrestrict.predicate.impl.common;

import com.stifflered.chatrestrict.predicate.UserInputPredicate;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public record ValidWeekDaysPredicate(Set<DayOfWeek> days) implements UserInputPredicate {

    @Override
    public boolean get(String input, Player sender) {
        return days.contains(DayOfWeek.from(OffsetDateTime.now()));
    }

    @Override
    public Component getRichDescription() {
        return Component.text("- If the current day of the week is any of: ").append(Component.text(days.toString()));
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
