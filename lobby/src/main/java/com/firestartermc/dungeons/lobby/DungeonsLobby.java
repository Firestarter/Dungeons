package com.firestartermc.dungeons.lobby;

import com.firestartermc.dungeons.lobby.commands.DungeonCommand;
import com.firestartermc.dungeons.lobby.listeners.PlayerListener;
import com.firestartermc.dungeons.lobby.util.NpcManager;
import com.firestartermc.dungeons.lobby.util.PacketReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nkomarn.kerosene.util.internal.Debug;

public class DungeonsLobby extends JavaPlugin implements Listener {

    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&6&lDungeons: &7");
    public static final String INSUFFICIENT_PERMISSIONS = ChatColor.translateAlternateColorCodes('&', "&c&lError: &7Insufficient permissions");
    public static final String DEBUG_CATEGORY_NPC_INTERACT = "dungeon:npc:interact";

    private static DungeonsLobby instance;

    private PacketReader packetReader;
    private NpcManager npcManager;
    private LobbyDungeonManager dungeonManager;

    @Override
    public void onEnable() {
        instance = this;
        Debug.registerCategory(DEBUG_CATEGORY_NPC_INTERACT);

        // Initialize
        this.saveDefaultConfig();
        this.npcManager = new NpcManager();
        this.packetReader = new PacketReader();
        this.dungeonManager = new LobbyDungeonManager();
        this.dungeonManager.syncAllData();

        // Command
        this.getCommand("dungeon").setExecutor(new DungeonCommand());

        // Listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.packetReader.inject(player);
        }
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.packetReader.unInject(player);
        }
        this.npcManager.despawn();
    }

    public PacketReader getPacketReader() {
        return packetReader;
    }

    public static DungeonsLobby getDungeonLobby() {
        return instance;
    }

    public static NpcManager getNpcManager() {
        return getDungeonLobby().npcManager;
    }

    public static LobbyDungeonManager getDungeonManager() {
        return getDungeonLobby().dungeonManager;
    }
}
