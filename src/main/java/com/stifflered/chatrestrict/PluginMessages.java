package com.stifflered.chatrestrict;

import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.*;
import net.kyori.adventure.text.minimessage.*;
import net.kyori.adventure.text.minimessage.tag.resolver.*;
import org.bukkit.configuration.*;

import java.util.*;
import java.util.concurrent.*;

public class PluginMessages {

    private final ConfigurationSection messageConfig;

    private final Map<String, Component> cachedSimpleMessages = new ConcurrentHashMap<>();

    public PluginMessages(ChatRestrictPlugin plugin) {
        this.messageConfig = plugin.getConfig().getConfigurationSection("messages");
    }

    public Component blockingEnables() {
        return simple("blocking-enabled");
    }

    public Component blockingDisabled() {
        return simple("blocking-disabled");
    }

    public Component notSent() {
        return simple("not-sent");
    }

    // Command messages
    public Component prefix() {
        return simple("command.prefix");
    }

    public Component noPermission() {
        return simple("command.no-permission");
    }

    public Component chatRestrictUsage() {
        return simple("command.chat-restrict.usage");
    }

    public Component chatRestrictReload(long ms) {
        return MiniMessage.miniMessage().deserialize(messageConfig.getString("command.chat-restrict.reload"), Placeholder.component("ms", Component.text(ms, NamedTextColor.WHITE)));
    }

    public Component overrideMessagesEnabled() {
        return simple("command.chat-restrict.override-enabled");
    }

    public Component overrideMessagesDisabled() {
        return simple("command.chat-restrict.override-disabled");
    }

    private Component simple(String key) {
        Component message = cachedSimpleMessages.get(key);
        if (message == null) {
            message = MiniMessage.miniMessage().deserialize(messageConfig.getString(key));
            cachedSimpleMessages.put(key, message);
        }

        return message;
    }

}
