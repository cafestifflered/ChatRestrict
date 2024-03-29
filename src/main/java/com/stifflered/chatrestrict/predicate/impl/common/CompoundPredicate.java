package com.stifflered.chatrestrict.predicate.impl.common;

import com.stifflered.chatrestrict.predicate.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Set;

public record CompoundPredicate(KeyedPredicate[] predicates) implements UserInputPredicate {

    @Override
    public RuleResult get(String input, Player sender) {
        // Stop on first false
        for (KeyedPredicate predicate : predicates) {
            if (!predicate.isEnabled()) {
                continue;
            }

            if (!sender.hasPermission("chatrestrict.bypass." + predicate.getName())) {
                RuleResult result = predicate.getPredicate().get(input, sender);
                if (!result.result()) {
                    return new RuleResult(false, predicate);
                }
            }
        }

        return RuleResult.TRUE;
    }

    @Override
    public Component getRichDescription() {
        TextComponent.Builder builder = Component.text();
        builder.content("All of the following conditions must return ");
        builder.append(Component.text(true, NamedTextColor.GREEN));

        int ignored = 0;
        for (KeyedPredicate predicate : predicates) {
            if (!predicate.isEnabled()) {
                ignored++;
                continue;
            }

            builder.append(Component.newline());
            builder.append(predicate.getPredicate().getRichDescription());
        }

        if (ignored > 0) {
            builder.append(Component.newline());
            builder.append(Component.text("(Hid " + ignored + " disabled rules)", NamedTextColor.GRAY, TextDecoration.ITALIC));
        }

        return builder.build();
    }

    public static class CompoundPredicateSerializer implements PredicateDeserializer<CompoundPredicate> {

        public static final CompoundPredicateSerializer INSTANCE = new CompoundPredicateSerializer();

        @Override
        public CompoundPredicate deserialize(ConfigurationSection section) {
            ConfigurationSection predicates = section.getConfigurationSection("rules");
            if (predicates == null) {
                return new CompoundPredicate(new KeyedPredicate[0]);
            }

            Set<String> predicateKeys = predicates.getKeys(false);
            KeyedPredicate[] userInputPredicates = new KeyedPredicate[predicateKeys.size()];

            int index = 0;
            for (String key : predicateKeys) {
                ConfigurationSection configurationSection = predicates.getConfigurationSection(key);

                KeyedPredicate predicate = new KeyedPredicate(key, configurationSection.getString("description"), UserInputPredicateRegistry.get(configurationSection));

                userInputPredicates[index] = predicate;
                index++;
            }

            return new CompoundPredicate(userInputPredicates);
        }
    }
}
