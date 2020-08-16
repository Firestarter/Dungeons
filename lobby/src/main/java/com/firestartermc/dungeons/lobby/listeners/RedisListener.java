package com.firestartermc.dungeons.lobby.listeners;

import com.firestartermc.dungeons.lobby.LobbyDungeonManager;
import com.firestartermc.dungeons.shared.Static;
import com.firestartermc.kerosene.event.PubSubMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RedisListener implements Listener {

    private final LobbyDungeonManager dungeonManager;

    public RedisListener(LobbyDungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onRedisMessage(PubSubMessageEvent event) {
        switch (event.getChannel()) {
            case Static.REDIS_SYNC_DUNGEONS:
                this.dungeonManager.syncDungeon(event.getMessage());
                break;
            case Static.REDIS_SYNC_PARTY:
                // TODO sync party
                break;
        }
    }

}
