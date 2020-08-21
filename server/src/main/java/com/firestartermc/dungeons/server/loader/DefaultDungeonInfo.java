package com.firestartermc.dungeons.server.loader;

import com.firestartermc.dungeons.shared.DungeonInfo;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DefaultDungeonInfo implements DungeonInfo {

    private String main;
    private String id;
    private String name;
    private List<String> lore;
    private String material;
    private int displayOrder;
    private int minPlayers;
    private int maxPlayers;
    private int maxInstances;

    protected DefaultDungeonInfo() {
        // default values
        this.lore = new ArrayList<>();
        this.material = Material.IRON_DOOR.name();
        this.displayOrder = 100;
        this.minPlayers = 2;
        this.maxPlayers = 10;
        this.maxInstances = 5;
    }

    public @NotNull String getMain() {
        return main;
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
        return this.lore;
    }

    @Override
    public @NotNull String getMaterial() {
        return this.material;
    }

    @Override
    public int getDisplayOrder() {
        return this.displayOrder;
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
