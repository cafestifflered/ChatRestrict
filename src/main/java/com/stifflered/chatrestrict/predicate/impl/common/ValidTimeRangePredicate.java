package com.stifflered.chatrestrict.predicate.impl.common;

import com.stifflered.chatrestrict.predicate.UserInputPredicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalQueries;
import java.util.function.Supplier;

public record ValidTimeRangePredicate(LocalTime min, LocalTime max,
                                      Supplier<ZoneOffset> offsetSupplier) implements UserInputPredicate {

    @Override
    public boolean get(String input, Player sender) {
        ZoneOffset offset = offsetSupplier.get();
        OffsetTime now = OffsetTime.now();

        return now.isAfter(min.atOffset(offset)) && now.isBefore(max.atOffset(offset));
    }

    @Override
    public Component getRichDescription() {
        return MiniMessage.miniMessage().deserialize("If the time (<timezone> offset) is within <min> and <max>.",
                Placeholder.component("timezone", Component.text(offsetSupplier.get().toString(), NamedTextColor.GREEN)),
                Placeholder.component("min", Component.text(min.toString(), NamedTextColor.GREEN)),
                Placeholder.component("max", Component.text(max.toString(), NamedTextColor.GREEN))
        );
    }

    public static class ValidTimeRangePredicateDeserializer implements PredicateDeserializer<ValidTimeRangePredicate> {

        private static Supplier<ZoneOffset> parseZone(String id) {
            // Parse offset
            try {
                ZoneOffset offset = ZoneOffset.of(id);
                return () -> offset;
            } catch (Exception ignored) {
            }

            // Parse timezone
            ZoneId timeZone;
            try {
                // Try short first
                timeZone = ZoneId.of(id, ZoneId.SHORT_IDS);
            } catch (Exception ignored) {
                try {
                    timeZone = ZoneId.of(id);
                } catch (Exception ignored2) {
                    throw new IllegalArgumentException("Could not parse valid timezone or time offset in %s".formatted(id));
                }
            }


            // Use a supplier to properly give a timed offset to ensure that when given a timezone it will correctly apply offsets
            // when dates change.
            ZoneId finalTimeZone = timeZone;
            return () -> finalTimeZone.getRules().getOffset(Instant.now());
        }

        private static LocalTime parseTime(String time) {
            return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).parse(time).query(TemporalQueries.localTime());
        }

        @Override
        public ValidTimeRangePredicate deserialize(ConfigurationSection section) {
            String min = section.getString("time_min");
            String max = section.getString("time_max");
            String zone = section.getString("zone");

            Supplier<ZoneOffset> parsedZone = zone == null ? () -> OffsetTime.now().getOffset() : parseZone(zone);
            LocalTime minTime;
            if (min == null) {
                minTime = LocalTime.MIDNIGHT;
            } else {
                minTime = parseTime(min);
            }

            LocalTime maxTime;
            if (max == null) {
                maxTime = LocalTime.MAX;
            } else {
                maxTime = parseTime(max);
            }

            return new ValidTimeRangePredicate(minTime, maxTime, parsedZone);
        }

    }
}
