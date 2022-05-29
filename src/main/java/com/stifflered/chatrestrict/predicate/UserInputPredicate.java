package com.stifflered.chatrestrict.predicate;

import org.bukkit.configuration.*;
import org.bukkit.entity.*;

/*
Returns a condition based on the given input that a player provides.
 */
public interface UserInputPredicate {

    boolean get(String input, Player sender);

    interface PredicateDeserializer<T extends UserInputPredicate> {

        T deserialize(ConfigurationSection section);

    }
}
