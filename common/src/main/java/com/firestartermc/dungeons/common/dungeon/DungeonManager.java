package com.firestartermc.dungeons.common.dungeon;

import com.firestartermc.dungeons.common.util.StreamUtil;
import com.google.common.collect.Maps;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import xyz.nkomarn.kerosene.data.redis.Redis;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class DungeonManager {

    public static final String DUNGEON_PREFIX = "dungeon:";

    protected final Map<String, DungeonInfo> dungeons;

    public DungeonManager() {
        this.dungeons = Maps.newHashMap();
    }

    public Set<DungeonInfo> getDungeons() {
        return new HashSet<>(this.dungeons.values());
    }

    public @Nonnull Optional<DungeonInfo> getDungeon(@Nonnull String id) {
        return Optional.ofNullable(this.dungeons.get(id));
    }

    public abstract void syncData(String id);

    public void syncDungeonData(@Nonnull DungeonInfo info) {
        try (Jedis jedis = Redis.getResource()) {
            Map<String, String> data = Maps.newHashMap();
            data.put("name", info.getName());
            data.put("minPlayers", String.valueOf(info.getMinPlayers()));
            data.put("maxPlayers", String.valueOf(info.getMaxPlayers()));
            data.put("instances", String.valueOf(info.getInstances()));
            data.put("maxInstances", String.valueOf(info.getMaxInstances()));
            data.put("material", info.getMaterial().name());
            jedis.hset(DUNGEON_PREFIX + info.getId(), data);
        }
    }

}
