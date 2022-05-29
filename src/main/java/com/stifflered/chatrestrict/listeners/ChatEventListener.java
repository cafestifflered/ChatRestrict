package com.stifflered.chatrestrict.listeners;

import com.stifflered.chatrestrict.*;
import io.papermc.paper.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class ChatEventListener implements Listener {

    private final ChatRestrictPlugin plugin;

    public ChatEventListener(ChatRestrictPlugin plugin) {
        this.plugin = plugin;
    }

    // Only call if it is not cancelled already. Allow other plugins to use if using for input
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void playerChatEvent(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPredicateHandler().canChat(event.originalMessage(), player) == plugin.getPredicateHandler().terminateOn()) {
            event.setCancelled(true);
            player.sendMessage(plugin.getPluginMessages().notSent());
        }
    }

}
