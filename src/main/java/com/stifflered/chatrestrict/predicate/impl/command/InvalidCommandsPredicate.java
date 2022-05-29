package com.stifflered.chatrestrict.predicate.impl.command;

import com.stifflered.chatrestrict.predicate.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

import java.util.*;

public record InvalidCommandsPredicate(List<String> commands) implements UserInputPredicate {

    @Override
    public boolean get(String input, Player sender) {
        if (input.startsWith("/")) {
            String rawInput = input.substring(1);

            for (String command : commands) {
                if (command.startsWith(rawInput)) {
                    return false;
                }
            }
        }

        return true; // Either input is not a command or is valid
    }

    public static class InvalidCommandsPredicateDeserializer implements PredicateDeserializer<InvalidCommandsPredicate> {

        @Override
        public InvalidCommandsPredicate deserialize(ConfigurationSection section) {
            List<String> badCommands = section.getStringList("commands");

            return new InvalidCommandsPredicate(badCommands);
        }
    }
}
