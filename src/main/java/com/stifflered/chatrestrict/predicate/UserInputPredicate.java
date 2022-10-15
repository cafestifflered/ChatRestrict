package com.stifflered.chatrestrict.predicate;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/*
Returns a condition based on the given input that a player provides.
 */
public interface UserInputPredicate {

    RuleResult get(String input, Player sender);

    Component getRichDescription();

    interface PredicateDeserializer<T extends UserInputPredicate> {

        T deserialize(ConfigurationSection section);

    }
}
