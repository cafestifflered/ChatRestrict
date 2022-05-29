package com.stifflered.chatrestrict.predicate.impl.common;

import com.stifflered.chatrestrict.*;
import com.stifflered.chatrestrict.predicate.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

public class DummyPredicate implements UserInputPredicate {

    // Return flipped so that this predicate is basically ignored
    @Override
    public boolean get(String input, Player sender) {
        return !ChatRestrictPlugin.getInstance().getPredicateHandler().terminateOn();
    }

    public static class DummyPredicateDeserializer implements PredicateDeserializer<DummyPredicate> {

        @Override
        public DummyPredicate deserialize(ConfigurationSection section) {
            return new DummyPredicate();
        }
    }
}
