package com.stifflered.chatrestrict.predicate.impl.command;

import com.stifflered.chatrestrict.predicate.RuleResult;
import com.stifflered.chatrestrict.predicate.UserInputPredicate;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public record InvalidCommandsPredicate(List<String> commands) implements UserInputPredicate {

    @Override
    public RuleResult get(String input, Player sender) {
        if (input.startsWith("/")) {
            String rawInput = input.substring(1);

            for (String command : commands) {
                if (command.startsWith(rawInput)) {
                    return RuleResult.of(false);
                }
            }
        }

        return RuleResult.of(true); // Either input is not a command or is valid
    }

    @Override
    public Component getRichDescription() {
        return Component.text("- If the player's command does not start with: ").append(Component.text(commands.toString()));
    }

    public static class InvalidCommandsPredicateDeserializer implements PredicateDeserializer<InvalidCommandsPredicate> {

        @Override
        public InvalidCommandsPredicate deserialize(ConfigurationSection section) {
            List<String> badCommands = section.getStringList("commands");

            return new InvalidCommandsPredicate(badCommands);
        }
    }
}
