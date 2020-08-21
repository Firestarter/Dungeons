package com.firestartermc.dungeons.server.loader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DungeonClassLoader extends URLClassLoader {

    private static final Set<DungeonClassLoader> loaders = new CopyOnWriteArraySet<>();

    public DungeonClassLoader(URL[] urls) {
        super(urls);
        loaders.add(this);
    }

}
