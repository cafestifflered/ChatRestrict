package com.stifflered.chatrestrict.predicate;

import com.stifflered.chatrestrict.predicate.impl.command.InvalidCommandsPredicate;
import com.stifflered.chatrestrict.predicate.impl.common.*;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class UserInputPredicateRegistry {

    private static final Map<String, UserInputPredicate.PredicateDeserializer<?>> PREDICATE_MAP = new HashMap<>();

    public static void bootstrap() {
        add("flip", new FlipPredicate.FlipSerializer());
        add("valid_weekdays", new ValidWeekDaysPredicate.ValidWeekDaysPredicateDeserializer());
        add("valid_timerange", new ValidTimeRangePredicate.ValidTimeRangePredicateDeserializer());
        add("invalid_commands", new InvalidCommandsPredicate.InvalidCommandsPredicateDeserializer());
        add("compound", CompoundPredicate.CompoundPredicateSerializer.INSTANCE);
        // Permission predicate?
    }

    public static UserInputPredicate get(ConfigurationSection section) {
        String id = section.getString("type");
        UserInputPredicate.PredicateDeserializer<?> deserializer = PREDICATE_MAP.get(id);
        if (deserializer == null) {
            throw new IllegalArgumentException("Could not find %s as a valid predicate!".formatted(id));
        }

        return deserializer.deserialize(section);
    }

    private static void add(String id, UserInputPredicate.PredicateDeserializer<?> predicateDeserializer) {
        PREDICATE_MAP.put(id, predicateDeserializer);
    }
}
