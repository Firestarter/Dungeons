package com.firestartermc.dungeons.lobby;

import com.firestartermc.dungeons.lobby.gui.DungeonSelectorGui;
import com.firestartermc.dungeons.shared.DungeonInfo;
import com.firestartermc.dungeons.shared.Static;
import com.google.common.collect.Maps;
import redis.clients.jedis.Jedis;
import xyz.nkomarn.kerosene.data.redis.Redis;
import xyz.nkomarn.kerosene.data.redis.RedisScript;
import xyz.nkomarn.kerosene.util.StreamUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LobbyDungeonManager {

    private RedisScript getAllDungeonDataScript;
    private final Map<String, DungeonInfo> dungeons;

    public LobbyDungeonManager() {
        super();

        this.dungeons = Maps.newHashMap();
        if (!this.loadLuaScripts()) {
            throw new RuntimeException("Unable to load lua script!");
        }

        this.syncAllData();
    }

    private boolean loadLuaScripts() {
        try {
            InputStream stream = DungeonsLobby.class.getResourceAsStream("/lua/get_all_dungeon_data.lua");
            String script = StreamUtil.streamToString(stream);
            this.getAllDungeonDataScript = RedisScript.of(script);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void syncDungeon(String id) {
        try (Jedis jedis = Redis.getResource()) {
            Map<String, String> redisData = jedis.hgetAll(Static.REDIS_DUNGEON_PREFIX + id);
            DungeonInfo info = LobbyDungeonInfo.of(redisData);

            this.dungeons.put(info.getId(), info);
        }
    }

    public void syncAllData() {
        DungeonsLobby.getInstance().getLogger().info("Syncing all dungeon data.");
        ArrayList<ArrayList<String>> dungeonsData = this.getAllDungeonDataScript.evalCast();

        this.dungeons.clear();
        for (ArrayList<String> data : dungeonsData) {
            Map<String, String> dungeonData = Maps.newHashMap();
            for(int i = 0; i < data.size(); i += 2) {
                String key = data.get(i);
                String value = data.get(i + 1);
                dungeonData.put(key, value);
            }

            DungeonInfo info = LobbyDungeonInfo.of(dungeonData);
            this.dungeons.put(info.getId(), info);
        }
        DungeonsLobby.getInstance().getLogger().info("Synced " + dungeonsData.size() + " dungeons.");
        DungeonSelectorGui.updateGui();
    }

    public List<DungeonInfo> getDungeons() {
        return null;
    }
}
