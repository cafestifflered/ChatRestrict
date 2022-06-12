package com.stifflered.chatrestrict.commands;

import com.stifflered.chatrestrict.ChatRestrictPlugin;
import com.stifflered.chatrestrict.PluginMessages;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class TempMuteallCommand extends BukkitCommand {

    private final ChatRestrictPlugin plugin;

    public TempMuteallCommand(ChatRestrictPlugin plugin) {
        super("tempmuteall");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        PluginMessages messages = plugin.getPluginMessages();

        if (!sender.hasPermission("chatrestrict.muteall.temp")) {
            sender.sendMessage(messages.noPermission());
            return true;
        }

        //this.plugin.getPredicateHandler().setIsChatRestricted(true);

        sender.sendMessage(messages.muteallMessagesEnabled());

        return false;
    }

}
