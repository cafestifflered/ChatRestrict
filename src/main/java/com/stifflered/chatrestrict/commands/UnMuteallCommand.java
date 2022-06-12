package com.stifflered.chatrestrict.commands;

import com.stifflered.chatrestrict.ChatRestrictPlugin;
import com.stifflered.chatrestrict.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UnMuteallCommand extends BukkitCommand {

    private final ChatRestrictPlugin plugin;

    public UnMuteallCommand(ChatRestrictPlugin plugin) {
        super("unmuteall");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        PluginMessages messages = plugin.getPluginMessages();

        if (!sender.hasPermission("chatrestrict.unmuteall")) {
            sender.sendMessage(messages.noPermission());
            return true;
        }

        this.plugin.getPredicateHandler().setIsChatRestricted(false);
        sender.sendMessage(messages.prefix().append(messages.muteallMessagesDisabled()));

        return false;
    }

}
