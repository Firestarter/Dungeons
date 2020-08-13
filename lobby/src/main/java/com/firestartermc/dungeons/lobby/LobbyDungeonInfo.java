package com.firestartermc.dungeons.lobby;

import com.firestartermc.dungeons.shared.DungeonInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LobbyDungeonInfo implements DungeonInfo {
    private final String id;
    private final String name;
    private final List<String> lore;
    private final String material;
    private final int order;
    private final int minPlayers;
    private final int maxPlayers;
    private final int maxInstances;

    public static DungeonInfo of(Map<String, String> dungeonData) {
        List<String> lore = new ArrayList<>();
        if (dungeonData.containsKey("lore")) {
            lore = Arrays.asList(dungeonData.get("lore").split(";;"));
        }
        
        return new LobbyDungeonInfo(
                dungeonData.get("id"),
                dungeonData.get("name"),
                lore,
                dungeonData.getOrDefault("material", "IRON_DOOR"),
                Integer.parseInt(dungeonData.getOrDefault("order", "100")),
                Integer.parseInt(dungeonData.getOrDefault("minPlayers", "2")),
                Integer.parseInt(dungeonData.getOrDefault("maxPlayers", "-1")),
                Integer.parseInt(dungeonData.getOrDefault("maxInstances", "-1"))
        );
    }

    private LobbyDungeonInfo(String id, String name, List<String> lore, String material, int order, int minPlayers, int maxPlayers, int maxInstances) {
        this.id = id;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.order = order;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.maxInstances = maxInstances;
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @Nullable String getName() {
        return this.name;
    }

    @Override
    public @NotNull List<String> getLore() {
        return lore;
    }

    @Override
    public @NotNull String getMaterial() {
        return this.material;
    }

    @Override
    public int getDisplayOrder() {
        return this.order;
    }

    @Override
    public int getMinPlayers() {
        return this.minPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    @Override
    public int getMaxInstances() {
        return this.maxInstances;
    }
}
