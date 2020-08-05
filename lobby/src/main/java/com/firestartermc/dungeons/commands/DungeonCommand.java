package com.firestartermc.dungeons.commands;

import com.firestartermc.dungeons.DungeonsLobby;
import com.firestartermc.dungeons.commands.base.BaseMultiCommand;
import org.bukkit.command.CommandSender;

public class DungeonCommand extends BaseMultiCommand {

    public DungeonCommand() {
        registerSubCommand("reload", new ReloadCommand());
        registerSubCommand("npc", new NpcCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, String alias, String[] args) {
        sender.sendMessage(DungeonsLobby.PREFIX + "Unknown command!"); // TODO replace with help text or something.
        return true;
    }
}
