package com.firestartermc.dungeons.lobby;

import com.firestartermc.dungeons.lobby.commands.DungeonCommand;
import com.firestartermc.dungeons.lobby.listeners.PlayerListener;
import com.firestartermc.dungeons.lobby.listeners.RedisListener;
import com.firestartermc.dungeons.lobby.util.NpcManager;
import com.firestartermc.dungeons.lobby.util.PacketReader;
import com.firestartermc.dungeons.shared.Static;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nkomarn.kerosene.data.redis.Redis;
import xyz.nkomarn.kerosene.util.internal.Debug;

public class DungeonsLobby extends JavaPlugin implements Listener {

    public static final String DEBUG_CATEGORY_NPC_INTERACT = Static.DEBUG_PREFIX + "npc:interact";

    private static DungeonsLobby instance;

    private PacketReader packetReader;
    private NpcManager npcManager;
    private LobbyDungeonManager dungeonManager;

    @Override
    public void onEnable() {
        instance = this;
        Debug.registerCategory(DEBUG_CATEGORY_NPC_INTERACT);
        Redis.subscribe(Static.REDIS_SYNC_DUNGEONS);

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
        getServer().getPluginManager().registerEvents(new RedisListener(this.dungeonManager), this);

        // Inject packet reader for all online players
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

        Redis.unsubscribe(Static.REDIS_SYNC_DUNGEONS);
    }

    public PacketReader getPacketReader() {
        return packetReader;
    }

    public static DungeonsLobby getInstance() {
        return instance;
    }

    public static NpcManager getNpcManager() {
        return getInstance().npcManager;
    }

    public static LobbyDungeonManager getDungeonManager() {
        return getInstance().dungeonManager;
    }
}
