package com.firestartermc.dungeons.server;

import com.firestartermc.dungeons.api.Dungeon;
import com.firestartermc.dungeons.server.loader.DungeonClassLoader;
import com.firestartermc.dungeons.server.loader.DungeonLoader;
import com.firestartermc.dungeons.server.loader.ServerDungeonInfo;
import com.firestartermc.dungeons.shared.DungeonInfo;
import com.firestartermc.dungeons.shared.Party;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerDungeonManager {

    private final Map<String, ServerDungeonInfo> dungeons = Maps.newHashMap();
    private final ListMultimap<String, Dungeon> enabledDungeons = ArrayListMultimap.create();

    private final DungeonLoader loader;

    public ServerDungeonManager() {
        this.loader = new DungeonLoader(this);
    }

    public void initialize() {
        List<ServerDungeonInfo> dungeons = this.scanDirectory();
        DungeonServer.get().getLogger().info(String.format(
                "Found %s dungeons. (%s)",
                dungeons.size(),
                dungeons.stream().map(ServerDungeonInfo::getId).collect(Collectors.joining(", "))
        ));
    }


    public List<ServerDungeonInfo> scanDirectory() {
        List<ServerDungeonInfo> dungeons = this.loader.scanDirectory();
        dungeons.forEach(dungeon -> this.dungeons.putIfAbsent(dungeon.getId(), dungeon));
        return dungeons;
    }

    private void startInstance(String id, Party party) {
        ServerDungeonInfo info = this.dungeons.get(id);
        if (info == null) {
            return; // return not found.
        }

        try {
            DungeonClassLoader loader = new DungeonClassLoader(new URL[]{ info.getJar().toUri().toURL() });
            Class<? extends Dungeon> mainClass = (Class<? extends Dungeon>) loader.loadClass(info.getMain());


        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
