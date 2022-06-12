package com.stifflered.chatrestrict.commands;

import com.stifflered.chatrestrict.ChatRestrictPlugin;
import com.stifflered.chatrestrict.PluginMessages;
import com.stifflered.chatrestrict.predicate.KeyedPredicate;
import com.stifflered.chatrestrict.predicate.PredicateHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
            case "enable", "disable" -> { // restrict
                if (!sender.hasPermission("chatrestrict.toggle")) {
                    sender.sendMessage(messages.noPermission());
                    return true;
                }

                String ruleKey = null;
                if (args.length > 1) {
                    ruleKey = args[1];
                }

                PredicateHandler predicateHandler = this.plugin.getPredicateHandler();
                boolean enabled = switch (args[0]) {
                    case "enable" -> true;
                    case "disable" -> false;
                    default -> throw new IllegalStateException(); // Shouldn't ever happen as it's checked above
                };

                KeyedPredicate ruleEnabled = predicateHandler.getRule(ruleKey);
                if (ruleEnabled == null && args.length > 1) {
                    sender.sendMessage(messages.prefix().append(messages.toggleRuleNotFound()));
                    return true;
                }

                if (ruleKey != null) {
                    ruleEnabled.setEnabled(enabled);

                    sender.sendMessage(
                            messages.prefix()
                                    .append(messages.toggleRule(
                                            Component.text(ruleKey),
                                            Component.text(enabled ? "enabled" : "disabled"))
                                    )
                                    .append(Component.newline())
                                    .append(
                                            ruleEnabled.getPredicate().getRichDescription()
                                    )
                    );
                } else {
                    predicateHandler.setAllRules(enabled);
                    if (enabled) {
                        sender.sendMessage(messages.prefix().append(messages.allRulesEnabled()));
                    } else {
                        sender.sendMessage(messages.prefix().append(messages.allRulesDisabled()));
                    }
                }

                return true;
            }
            case "debug" -> {
                if (!sender.hasPermission("chatrestrict.debug")) {
                    sender.sendMessage(messages.noPermission());
                    return true;
                }

                sender.sendMessage(Component.text("Debugging ChatRestrict:"));
                sender.sendMessage(Component.text("Is Chat Restricted? " + this.plugin.getPredicateHandler().isChatRestricted()));
                sender.sendMessage(Component.text("Backing Predicate " + this.plugin.getPredicateHandler().getCompoundPredicate()));
                for (KeyedPredicate predicate : this.plugin.getPredicateHandler().getCompoundPredicate().predicates()) {
                    sender.sendMessage(Component.text("Predicate Element: ").append(Component.text(predicate.getClass().getName(), NamedTextColor.GREEN)));
                    sender.sendMessage(Component.text("Predicate Test: ").append(Component.text(predicate.getPredicate().get("test", (Player) sender), NamedTextColor.GOLD)));
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
            return List.of("reload", "enable", "disable"    );
        } else if (args.length > 1 && (args[0].equals("enable") || args[0].equals("disable"))) {
            List<String> strings = new ArrayList<>();
            for (KeyedPredicate predicate : this.plugin.getPredicateHandler().getRules()) {
                strings.add(predicate.getName());
            }

            return strings;
        }

        return List.of();
    }
}
