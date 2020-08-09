package com.firestartermc.dungeons.common.dungeon;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DungeonInfo {

    private final String id;
    private String name;
    private int minPlayers;
    private int maxPlayers;
    private int maxInstances;
    private Material material;
    private int order;

    public DungeonInfo(@NotNull String id, @NotNull String name, int minPlayers, int maxPlayers, int maxInstances, @Nullable Material material, int order) {
        this.id = id;
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.maxInstances = maxInstances;
        this.material = material != null ? material : Material.IRON_DOOR;
        this.order = order;
    }

    public DungeonInfo(@NotNull Map<String, String> data) {
        this.id = data.get("id");
        this.name = data.get("name");
        this.minPlayers = Integer.parseInt(data.getOrDefault("minPlayers", "1"));
        this.maxPlayers = Integer.parseInt(data.getOrDefault("maxPlayers", "-1"));
        this.maxInstances = Integer.parseInt(data.getOrDefault("maxInstances", "-1"));
        this.material = Material.valueOf(data.getOrDefault("material", Material.IRON_DOOR.name()));
        this.order = Integer.parseInt(data.getOrDefault("order", "100"));
    }

    public Map<String, String> toMap() {
        Map<String, String> map = Maps.newHashMap();
        map.put("id", this.id);
        map.put("name", this.name);
        map.put("minPlayers", String.valueOf(this.minPlayers));
        map.put("maxPlayers", String.valueOf(this.maxPlayers));
        map.put("maxInstances", String.valueOf(this.maxInstances));
        map.put("material", this.material.name());
        return map;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getInstances() {
        return -1; // TODO load from redis
    }

    public int getMaxInstances() {
        return maxInstances;
    }

    public Material getMaterial() {
        return material;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "DungeonInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", minPlayers=" + minPlayers +
                ", maxPlayers=" + maxPlayers +
                ", maxInstances=" + maxInstances +
                ", material=" + material.name() +
                '}';
    }
}
