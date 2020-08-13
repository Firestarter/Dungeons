package com.firestartermc.dungeons.lobby.commands;

import com.firestartermc.dungeons.lobby.commands.base.BaseMultiCommand;
import com.firestartermc.dungeons.shared.Static;
import org.bukkit.command.CommandSender;

public class DungeonCommand extends BaseMultiCommand {

    public DungeonCommand() {
        registerSubCommand("reload", new ReloadCommand());
        registerSubCommand("npc", new NpcCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, String alias, String[] args) {
        sender.sendMessage(Static.PREFIX + "Unknown command!"); // TODO replace with help text or something.
        return true;
    }
}
