package com.firestartermc.dungeons.lobby;

import com.firestartermc.dungeons.lobby.commands.DungeonCommand;
import com.firestartermc.dungeons.lobby.listeners.PlayerListener;
import com.firestartermc.dungeons.lobby.listeners.RedisListener;
import com.firestartermc.dungeons.lobby.npc.NpcManager;
import com.firestartermc.dungeons.shared.Static;
import com.firestartermc.kerosene.util.internal.Debug;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DungeonsLobby extends JavaPlugin implements Listener {

    public static final String DEBUG_CATEGORY_NPC_INTERACT = Static.DEBUG_PREFIX + "npc:interact";

    private static DungeonsLobby instance;

    private LobbyDungeonManager dungeonManager;
    private NpcManager npcManager;
//    private PacketReader packetReader;

    @Override
    public void onEnable() {
        instance = this;
        Debug.registerCategory(DEBUG_CATEGORY_NPC_INTERACT);
        // Redis.subscribe(Static.REDIS_SYNC_DUNGEONS);

        // Initialize
        this.saveDefaultConfig();
        this.dungeonManager = new LobbyDungeonManager();
        this.dungeonManager.syncAllData();
        this.npcManager = new NpcManager();
        this.npcManager.spawnAll();
//        this.packetReader = new PacketReader();

        // Command
        this.getCommand("dungeon").setExecutor(new DungeonCommand());

        // Listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new RedisListener(this.dungeonManager), this);

        // Inject packet reader for all online players
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            this.packetReader.inject(player);
//        }
    }

    @Override
    public void onDisable() {
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            this.packetReader.unInject(player);
//        }
        this.npcManager.despawnAll();

        // Redis.unsubscribe(Static.REDIS_SYNC_DUNGEONS);
    }

//    public PacketReader getPacketReader() {
//        return packetReader;
//    }

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
