package com.stifflered.chatrestrict.predicate.impl.common;

import com.stifflered.chatrestrict.predicate.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.function.*;

public record ValidTimeRangePredicate(LocalTime min, LocalTime max, Supplier<ZoneOffset> offsetSupplier) implements UserInputPredicate {

    @Override
    public boolean get(String input, Player sender) {
        ZoneOffset offset = offsetSupplier.get();
        OffsetTime now = OffsetTime.now();

        return now.isAfter(min.atOffset(offset)) && now.isBefore(max.atOffset(offset));
    }

    public static class ValidTimeRangePredicateDeserializer implements PredicateDeserializer<ValidTimeRangePredicate> {

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

    }
}
