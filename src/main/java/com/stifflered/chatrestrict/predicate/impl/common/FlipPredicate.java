package com.stifflered.chatrestrict.predicate.impl.common;

import com.stifflered.chatrestrict.predicate.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

public record FlipPredicate(UserInputPredicate innerInput) implements UserInputPredicate {

    @Override
    public boolean get(String input, Player sender) {
        return !innerInput.get(input, sender);
    }

    public static class FlipSerializer implements UserInputPredicate.PredicateDeserializer<FlipPredicate> {

        @Override
        public FlipPredicate deserialize(ConfigurationSection section) {
            ConfigurationSection predicate = section.getConfigurationSection("predicate");

            return new FlipPredicate(UserInputPredicateRegistry.get(predicate));
        }
    }
}
