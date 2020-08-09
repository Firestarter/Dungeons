package com.firestartermc.dungeons.lobby.commands;

import com.firestartermc.dungeons.lobby.DungeonsLobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReloadCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("firestarter.dungeons.reload")) {
            commandSender.sendMessage(DungeonsLobby.INSUFFICIENT_PERMISSIONS);
            return true;
        }

        // Reload config
        DungeonsLobby.getDungeonLobby().reloadConfig();

        // Reload redis data
        DungeonsLobby.getDungeonManager().syncAllData();

        // Reload npc
        DungeonsLobby.getNpcManager().reload();

        commandSender.sendMessage(DungeonsLobby.PREFIX + "Reloaded dungeons");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
