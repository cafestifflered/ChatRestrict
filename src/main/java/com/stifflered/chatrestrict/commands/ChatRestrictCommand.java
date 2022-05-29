package com.stifflered.chatrestrict.commands;

import com.stifflered.chatrestrict.*;
import com.stifflered.chatrestrict.predicate.*;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.*;
import org.bukkit.command.*;
import org.bukkit.command.defaults.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ChatRestrictCommand extends BukkitCommand {

    private final ChatRestrictPlugin plugin;

    public ChatRestrictCommand(ChatRestrictPlugin plugin) {
        super("chatrestrict");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        PluginMessages messages = plugin.getPluginMessages();
        if (args.length == 0) {
            sender.sendMessage(messages.chatRestrictUsage());
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                if (!sender.hasPermission("chatrestrict.reload")) {
                    sender.sendMessage(messages.noPermission());
                    return true;
                }

                long ms = System.currentTimeMillis();
                this.plugin.reloadConfigs();
                ms = System.currentTimeMillis() - ms;
                messages = this.plugin.getPluginMessages();

                sender.sendMessage(messages.prefix().append(messages.chatRestrictReload(ms)));

                return true;
            }
            case "enable", "disable", "toggle" -> { // restrict
                if (!sender.hasPermission("chatrestrict.toggle")) {
                    sender.sendMessage(messages.noPermission());
                    return true;
                }

                PredicateHandler predicateHandler = this.plugin.getPredicateHandler();
                boolean override = switch (args[0]) {
                    case "enable" -> true;
                    case "disable" -> false;
                    case "toggle" -> !predicateHandler.isChatRestricted();
                    default -> throw new IllegalStateException(); // Shouldn't ever happen as it's checked above
                };

                if (override) {
                    sender.sendMessage(messages.prefix().append(messages.overrideMessagesEnabled()));
                } else {
                    sender.sendMessage(messages.prefix().append(messages.overrideMessagesDisabled()));
                }

                predicateHandler.setIsChatRestricted(override);

                return true;
            }
            case "debug" -> {
                if (!sender.hasPermission("chatrestrict.debug")) {
                    sender.sendMessage(messages.noPermission());
                    return true;
                }

                sender.sendMessage(Component.text("Debugging ChatRestrict:"));
                sender.sendMessage(Component.text("Is Chat Restricted? " + this.plugin.getPredicateHandler().isChatRestricted()));
                sender.sendMessage(Component.text("Terminate On: " + this.plugin.getPredicateHandler().terminateOn()));
                sender.sendMessage(Component.text("Backing Predicate " + this.plugin.getPredicateHandler().getCompoundPredicate()));
                for (UserInputPredicate predicate : this.plugin.getPredicateHandler().getCompoundPredicate().predicates()) {
                    sender.sendMessage(Component.text("Predicate Element: ").append(Component.text(predicate.getClass().getName(), NamedTextColor.GREEN)));
                    sender.sendMessage(Component.text("Predicate Test: ").append(Component.text(predicate.get("test", (Player) sender), NamedTextColor.GOLD)));
                }

                return true;
            }
        }
        sender.sendMessage(messages.chatRestrictUsage());

        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return List.of("reload", "enable", "disable", "toggle");
        }

        return List.of();
    }
}
