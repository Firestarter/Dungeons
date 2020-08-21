package com.firestartermc.dungeons.server.commands;

import com.firestartermc.dungeons.server.commands.base.BaseMultiCommand;

public class DungeonCommand extends BaseMultiCommand {

    public DungeonCommand() {
        registerSubCommand("dungeons", new DungeonsCommand(), "d", "dun", "dungeon");
    }

}
