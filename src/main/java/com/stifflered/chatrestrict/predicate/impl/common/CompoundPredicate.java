package com.stifflered.chatrestrict.predicate.impl.common;

import com.stifflered.chatrestrict.predicate.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.logging.*;

public record CompoundPredicate(UserInputPredicate[] predicates) implements UserInputPredicate {

    @Override
    public boolean get(String input, Player sender) {
        // Stop on first false
        for (UserInputPredicate predicate : predicates) {
            if (!predicate.get(input, sender)) {
                return false;
            }
        }

        return true;
    }

    public static class CompoundPredicateSerializer implements PredicateDeserializer<CompoundPredicate> {

        public static final CompoundPredicateSerializer INSTANCE = new CompoundPredicateSerializer();

        @Override
        public CompoundPredicate deserialize(ConfigurationSection section) {
            ConfigurationSection predicates = section.getConfigurationSection("predicates");
            Set<String> predicateKeys = predicates.getKeys(false);
            UserInputPredicate[] userInputPredicates = new UserInputPredicate[predicateKeys.size()];

            int index = 0;
            for (String key : predicateKeys) {
                userInputPredicates[index] = UserInputPredicateRegistry.get(predicates.getConfigurationSection(key));

                index++;
            }

            return new CompoundPredicate(userInputPredicates);
        }
    }
}
