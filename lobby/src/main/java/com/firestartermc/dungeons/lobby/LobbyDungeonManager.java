package com.firestartermc.dungeons.lobby;

import com.firestartermc.dungeons.common.dungeon.DungeonInfo;
import com.firestartermc.dungeons.common.dungeon.DungeonManager;
import com.firestartermc.dungeons.common.util.StreamUtil;
import com.firestartermc.dungeons.lobby.gui.DungeonSelectorGui;
import com.google.common.collect.Maps;
import xyz.nkomarn.kerosene.data.redis.RedisScript;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class LobbyDungeonManager extends DungeonManager {

    private RedisScript getAllDungeonDataScript;

    public LobbyDungeonManager() {
        super();

        InputStream stream = DungeonsLobby.class.getResourceAsStream("/lua/get_all_dungeon_data.lua");
        String script = StreamUtil.streamToString(stream);
        if (script == null) {
            throw new RuntimeException("Unable to load script!");
        }

        this.getAllDungeonDataScript = RedisScript.of(script);
        this.syncAllData();
    }

    @Override
    public void syncData(String id) {
        // HGETALL, PREFIX + id
    }

    public void syncAllData() {
        DungeonsLobby.getDungeonLobby().getLogger().info("Syncing all dungeon data.");
        ArrayList<ArrayList<String>> dungeonsData = this.getAllDungeonDataScript.evalCast();

        this.dungeons.clear();
        for (ArrayList<String> data : dungeonsData) {
            Map<String, String> dungeonData = Maps.newHashMap();
            for(int i = 0; i < data.size(); i += 2) {
                String key = data.get(i);
                String value = data.get(i + 1);
                dungeonData.put(key, value);
            }

            DungeonInfo info = new DungeonInfo(dungeonData);
            this.dungeons.put(info.getId(), info);
        }
        DungeonsLobby.getDungeonLobby().getLogger().info("Synced " + dungeonsData.size() + " dungeons.");
        DungeonSelectorGui.updateGui();
    }

}
