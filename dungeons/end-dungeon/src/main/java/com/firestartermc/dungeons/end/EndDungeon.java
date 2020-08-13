package com.firestartermc.dungeons.end;

import com.firestartermc.dungeons.api.Dungeon;

public class EndDungeon implements Dungeon {

    @Override
    public boolean setup() {
        return false;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean end() {
        return false;
    }

    @Override
    public boolean cleanup() {
        return false;
    }
}
