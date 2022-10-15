package com.stifflered.chatrestrict.listeners;

import com.stifflered.chatrestrict.ChatRestrictPlugin;
import com.stifflered.chatrestrict.PluginMessages;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignEventListener implements Listener {

    private final ChatRestrictPlugin plugin;

    public SignEventListener(ChatRestrictPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void signEvent(SignChangeEvent event) {
        if (!plugin.isRestrictingExtra()) {
            return;
        }

        Player player = event.getPlayer();

        if (!plugin.getPredicateHandler().canChat(Component.text("DUMMY SIGN INFO"), player).result()) {
            PluginMessages messages = plugin.getPluginMessages();
            player.sendMessage(messages.prefix().append(messages.cantusethis()));
            event.setCancelled(true);
        }
    }
}
