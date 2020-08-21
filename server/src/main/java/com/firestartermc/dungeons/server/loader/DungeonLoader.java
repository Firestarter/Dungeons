package com.firestartermc.dungeons.server.loader;

import com.firestartermc.dungeons.server.DungeonServer;
import com.firestartermc.dungeons.server.ServerDungeonManager;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class DungeonLoader {

    private final ServerDungeonManager dungeonManager;
    private final Path dungeonFolder;

    public DungeonLoader(ServerDungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
        this.dungeonFolder = DungeonServer.get().getDataFolder().toPath().resolve("dungeons");
    }

    public List<ServerDungeonInfo> scanDirectory() {
        try (DirectoryStream<Path> pathStream = Files.newDirectoryStream(this.dungeonFolder, p -> p.endsWith(".jar"))) {
            List<ServerDungeonInfo> newDungeons = new ArrayList<>();
            for (Path path : pathStream) {
                ServerDungeonInfo info = loadFromJar(path);
                if (info == null) continue;
                newDungeons.add(info);
            }
            return newDungeons;
        } catch (IOException e) {
            e.printStackTrace();
            DungeonServer.get().getLogger().severe("Failed to get dungeon folder stream.");
            return ImmutableList.of();
        }
    }

    private @Nullable ServerDungeonInfo loadFromJar(Path path) {
        try (JarInputStream in = new JarInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            JarEntry entry;
            while ((entry = in.getNextJarEntry()) != null) {
                if (!entry.getName().equals("dungeon.yml")) {
                    continue;
                }

                try (Reader dungeonInfoReader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                    DefaultDungeonInfo defaultDungeonInfo = new Yaml().load(dungeonInfoReader);
                    // TODO load the overridden info properties.
                    return new ServerDungeonInfo(defaultDungeonInfo, path);
                }
            }

            DungeonServer.get().getLogger().severe("Successfully read '" + path.getFileName() + "' but it doesn't contain a 'dungeon.yml' file.");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            DungeonServer.get().getLogger().severe("An error occurred attempting to read '" + path.getFileName() + "'.");
            return null;
        }

    }

}
