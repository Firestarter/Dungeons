package com.firestartermc.dungeons.shared;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DungeonInfo {

    @NotNull String getId();

    @Nullable String getName();

    @NotNull
    List<String> getLore();

    @NotNull String getMaterial();

    int getDisplayOrder();

    int getMinPlayers();

    int getMaxPlayers();

    int getMaxInstances();
}
