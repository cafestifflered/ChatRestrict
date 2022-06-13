package com.stifflered.chatrestrict;

import com.google.common.base.Charsets;
import com.stifflered.chatrestrict.commands.*;
import com.stifflered.chatrestrict.listeners.ChatEventListener;
import com.stifflered.chatrestrict.predicate.PredicateHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ChatRestrictPlugin extends JavaPlugin {

    private static ChatRestrictPlugin instance;
    private PredicateHandler predicateHandler;
    private PluginMessages messages;
    private static Logger logger;

    public static ChatRestrictPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        this.predicateHandler = new PredicateHandler(this);
        this.messages = new PluginMessages(this);
        logger = getSLF4JLogger();
        instance = this;

        Bukkit.getPluginManager().registerEvents(new ChatEventListener(this), this);
        register(new ChatRestrictCommand(this));
        register(new MuteallCommand(this));
        //register(new TempMuteallCommand(this));
        register(new UnMuteallCommand(this));
    }

    private void register(Command command) {
        Bukkit.getCommandMap().register("chatrestrict", command);
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

    public FileConfiguration getRules() {
        return saveDefaultResourceOrGet("rules.yml");
    }

    public PredicateHandler getPredicateHandler() {
        return predicateHandler;
    }

    public PluginMessages getPluginMessages() {
        return messages;
    }

    private FileConfiguration saveDefaultResourceOrGet(String key) {
        File file = new File(getDataFolder(), key);
        if (!file.exists()) {
            saveResource(key, false);
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        final InputStream defConfigStream = getResource(key);
        if (defConfigStream == null) {
            return configuration;
        }

        configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));

        return configuration;
    }

    private void saveResource(String key, FileConfiguration configuration) {
        File file = new File(getDataFolder(), key);
        try {
            configuration.save(file);
        } catch (Exception e) {
            logger.error("Failed to save file!", e);
        }
    }

    public boolean isLogging() {
        return getConfig().getBoolean("verbose-logging");
    }
}
