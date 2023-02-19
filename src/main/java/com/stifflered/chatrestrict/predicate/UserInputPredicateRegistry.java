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
        add("allowed_weekdays", new ValidWeekDaysPredicate.ValidWeekDaysPredicateDeserializer());
        add("allowed_timerange", new ValidTimeRangePredicate.ValidTimeRangePredicateDeserializer());
        add("disallowed_commands", new InvalidCommandsPredicate.InvalidCommandsPredicateDeserializer());
        add("online_player_with_permission", new PlayerPermissionPredicate.PlayerPermissionPredicateDeserializer());
        add("ruleset", CompoundPredicate.CompoundPredicateSerializer.INSTANCE);
    }

    public static UserInputPredicate get(ConfigurationSection section) {
        String id = section.getString("type");
        UserInputPredicate.PredicateDeserializer<?> deserializer = PREDICATE_MAP.get(id);
        if (deserializer == null) {
            throw new IllegalArgumentException("Could not find %s as a valid rule!".formatted(id));
        }

        return deserializer.deserialize(section);
    }

    private static void add(String id, UserInputPredicate.PredicateDeserializer<?> predicateDeserializer) {
        PREDICATE_MAP.put(id, predicateDeserializer);
    }
}
