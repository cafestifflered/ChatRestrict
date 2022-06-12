package com.stifflered.chatrestrict;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class PluginMessages {

    private final ConfigurationSection messageConfig;

    private final Map<String, Component> cachedSimpleMessages = new ConcurrentHashMap<>();

    public PluginMessages(ChatRestrictPlugin plugin) {
        this.messageConfig = plugin.getConfig().getConfigurationSection("messages");
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

    public Component allRulesEnabled() {
        return simple("command.chat-restrict.allrules-enabled");
    }

    public Component allRulesDisabled() {
        return simple("command.chat-restrict.allrules-disabled");
    }

    public Component chatRestrictReload(long ms) {
        return MiniMessage.miniMessage().deserialize(messageConfig.getString("command.chat-restrict.reload"), Placeholder.component("ms", Component.text(ms, NamedTextColor.WHITE)));
    }

    public Component muteallMessagesEnabled() {
        return simple("command.chat-restrict.muteall-enabled");
    }

    public Component muteallMessagesDisabled() {
        return simple("command.chat-restrict.muteall-disabled");
    }

    public Component toggleRule(Component rulename, Component status) {
        return MiniMessage.miniMessage().deserialize(messageConfig.getString("command.chat-restrict.toggle-rule"),
                Placeholder.component("rulename", rulename),
                Placeholder.component("status", status)
        );
    }

    public Component toggleRuleNotFound() {
        return simple("command.chat-restrict.toggle-rule-not-found");
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
