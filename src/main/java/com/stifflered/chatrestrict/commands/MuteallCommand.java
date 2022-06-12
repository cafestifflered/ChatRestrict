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

import java.util.List;

public class MuteallCommand extends BukkitCommand {

    private final ChatRestrictPlugin plugin;

    public MuteallCommand(ChatRestrictPlugin plugin) {
        super("muteall");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        PluginMessages messages = plugin.getPluginMessages();

        if (!sender.hasPermission("chatrestrict.muteall")) {
            sender.sendMessage(messages.noPermission());
            return true;
        }

        this.plugin.getPredicateHandler().setIsChatRestricted(true);
        sender.sendMessage(messages.prefix().append(messages.muteallMessagesEnabled()));

        return false;
    }

}
