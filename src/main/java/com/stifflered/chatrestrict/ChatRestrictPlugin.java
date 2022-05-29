package com.stifflered.chatrestrict;

import com.stifflered.chatrestrict.commands.*;
import com.stifflered.chatrestrict.listeners.*;
import com.stifflered.chatrestrict.predicate.*;
import org.bukkit.*;
import org.bukkit.plugin.java.*;

public class ChatRestrictPlugin extends JavaPlugin {

    private PredicateHandler predicateHandler;
    private PluginMessages messages;

    private static ChatRestrictPlugin instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        this.predicateHandler = new PredicateHandler(this);
        this.messages = new PluginMessages(this);
        instance = this;

        Bukkit.getPluginManager().registerEvents(new ChatEventListener(this), this);
        Bukkit.getCommandMap().register("chatrestrict", new ChatRestrictCommand(this));
    }

    @Override
    public void onDisable() {
        this.predicateHandler = null;
        this.messages = null;
        instance = null;
        this.saveConfig();
    }

    public void reloadConfigs() {
        reloadConfig();
        this.predicateHandler = new PredicateHandler(this);
        this.messages = new PluginMessages(this);
    }

    public PredicateHandler getPredicateHandler() {
        return predicateHandler;
    }

    public PluginMessages getPluginMessages() {
        return messages;
    }

    public static ChatRestrictPlugin getInstance() {
        return instance;
    }
}
