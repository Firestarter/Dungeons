package com.firestartermc.dungeons.server.loader;

import com.firestartermc.dungeons.shared.DungeonInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.OptionalInt;

public class ServerDungeonInfo implements DungeonInfo {

    private final DefaultDungeonInfo defaultInfo;
    private final Path jar;
    private String name;
    private List<String> lore;
    private String material;
    private OptionalInt displayOrder;
    private OptionalInt minPlayers;
    private OptionalInt maxPlayers;
    private OptionalInt maxInstances;

    public ServerDungeonInfo(DefaultDungeonInfo defaultInfo, Path jar) {
        this.defaultInfo = defaultInfo;
        this.jar = jar;
        this.displayOrder = OptionalInt.empty();
        this.minPlayers = OptionalInt.empty();
        this.maxPlayers = OptionalInt.empty();
        this.maxInstances = OptionalInt.empty();
    }

    public @NotNull String getMain() {
        return this.defaultInfo.getMain();
    }

    public Path getJar() {
        return jar;
    }

    @Override
    public @NotNull String getId() {
        return this.defaultInfo.getId();
    }

    @Override
    public @Nullable String getName() {
        return this.defaultInfo.getName();
    }

    @Override
    public @NotNull List<String> getLore() {
        return this.lore != null ? this.lore : this.defaultInfo.getLore();
    }

    @Override
    public @NotNull String getMaterial() {
        return this.material != null ? this.material : this.defaultInfo.getMaterial();
    }

    @Override
    public int getDisplayOrder() {
        return this.displayOrder.orElse(this.defaultInfo.getDisplayOrder());
    }

    @Override
    public int getMinPlayers() {
        return this.minPlayers.orElse(this.defaultInfo.getMinPlayers());
    }

    @Override
    public int getMaxPlayers() {
        return this.maxPlayers.orElse(this.defaultInfo.getMaxPlayers());
    }

    @Override
    public int getMaxInstances() {
        return this.maxInstances.orElse(this.defaultInfo.getMaxInstances());
    }
}
