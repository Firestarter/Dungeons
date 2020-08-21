package com.firestartermc.dungeons.server;

import org.bukkit.plugin.java.JavaPlugin;

public class DungeonServer extends JavaPlugin {

    private static DungeonServer instance;

    private ServerDungeonManager dungeonManager;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Initializing");
        this.saveDefaultConfig();
        this.dungeonManager = new ServerDungeonManager();

        getLogger().info("Loading dungeons");
        this.dungeonManager.initialize();
    }

    @Override
    public void onDisable() {
        // unload dungeons
    }

    public static DungeonServer get() {
        return instance;
    }

    public static ServerDungeonManager getDungeonManager() {
        return get().dungeonManager;
    }
}
