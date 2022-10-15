package com.stifflered.chatrestrict.predicate.impl.common;

import com.stifflered.chatrestrict.predicate.RuleResult;
import com.stifflered.chatrestrict.predicate.UserInputPredicate;
import com.stifflered.chatrestrict.predicate.UserInputPredicateRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public record FlipPredicate(UserInputPredicate innerInput) implements UserInputPredicate {

    @Override
    public RuleResult get(String input, Player sender) {
        RuleResult result = innerInput.get(input, sender);
        return new RuleResult(!result.result(), result.broken());
    }

    @Override
    public Component getRichDescription() {
        return Component.text("- NOT: ").append(innerInput.getRichDescription());
    }

    public static class FlipSerializer implements UserInputPredicate.PredicateDeserializer<FlipPredicate> {

        @Override
        public FlipPredicate deserialize(ConfigurationSection section) {
            ConfigurationSection predicate = section.getConfigurationSection("rule");

            return new FlipPredicate(UserInputPredicateRegistry.get(predicate));
        }
    }
}
