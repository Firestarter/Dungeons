package com.firestartermc.dungeons.server;

import org.bukkit.plugin.java.JavaPlugin;

public class DungeonServer extends JavaPlugin {

    private static DungeonServer instance;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {

    }

    public static DungeonServer getInstance() {
        return instance;
    }
}
