package com.stifflered.chatrestrict.listeners;

import com.stifflered.chatrestrict.ChatRestrictPlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatEventListener implements Listener {

    private final ChatRestrictPlugin plugin;

    public ChatEventListener(ChatRestrictPlugin plugin) {
        this.plugin = plugin;
    }

    // Only call if it is not cancelled already. Allow other plugins to use if using for input
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void playerChatEvent(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getPredicateHandler().canChat(event.originalMessage(), player)) {
            event.setCancelled(true);
            player.sendMessage(plugin.getPluginMessages().notSent());
        }
    }

}
