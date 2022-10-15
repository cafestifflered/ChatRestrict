package com.stifflered.chatrestrict.listeners;

import com.stifflered.chatrestrict.ChatRestrictPlugin;
import com.stifflered.chatrestrict.PluginMessages;
import com.stifflered.chatrestrict.predicate.RuleResult;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatEventListener implements Listener {

    private final ChatRestrictPlugin plugin;

    public ChatEventListener(ChatRestrictPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void playerChatEvent(AsyncChatEvent event) {
        Player player = event.getPlayer();

        RuleResult result = plugin.getPredicateHandler().canChat(event.originalMessage(), player);
        PluginMessages messages = plugin.getPluginMessages();

        if (!result.result()) {
            if (plugin.shouldKick()) {
                Component kickMessage;
                if (player.hasPermission("chatrestrict.description")) {
                    kickMessage = messages.kickMsg()
                            .append(Component.newline())
                            .append(messages.ruleBrokenDescription(result.broken()));
                } else {
                    kickMessage = messages.kickMsg();
                }

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        player.kick(kickMessage);
                    }
                }.runTask(plugin);
            } else {
                event.setCancelled(true);
                Component responseMessage = messages.prefix().append(messages.notSent());
                if (player.hasPermission("chatrestrict.description") && result.hasDescription()) {
                    responseMessage = responseMessage
                            .append(Component.newline())
                            .append(messages.ruleBrokenDescription(result.broken()));
                }

                player.sendMessage(responseMessage);
            }

            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff != player && staff.hasPermission("chatrestrict.description.others")) {
                    Component responseMessage = plugin.getPluginMessages().playerBrokeChatRule(player.getName());
                    if (result.hasDescription()) {
                        responseMessage = responseMessage
                                .append(Component.newline())
                                .append(messages.ruleBrokenDescription(result.broken()));
                    }

                    staff.sendMessage(responseMessage);
                }
            }
        }
    }

}
