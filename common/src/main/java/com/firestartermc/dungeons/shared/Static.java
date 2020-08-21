package com.firestartermc.dungeons.shared;

import org.bukkit.ChatColor;

public final class Static {
    private Static() {
    }

    public static final String DUNGEON_PREFIX = "dungeons:";

    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&6&lDungeons: &7");
    public static final String DEBUG_PREFIX = DUNGEON_PREFIX;

    public static final String REDIS_DUNGEON_PREFIX = DUNGEON_PREFIX;
    public static final String REDIS_SYNC_DUNGEONS = DUNGEON_PREFIX + "sync:dungeon";
    public static final String REDIS_SYNC_PARTY = DUNGEON_PREFIX + "sync:party";
}
