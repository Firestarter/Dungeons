package com.firestartermc.dungeons.commands;

import com.firestartermc.dungeons.DungeonsLobby;
import org.bukkit.ChatColor;
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

        // Reload npc
        DungeonsLobby.getNpcManager().despawn();
        DungeonsLobby.getNpcManager().spawn();

        commandSender.sendMessage(DungeonsLobby.PREFIX + "Reloaded dungeons");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
