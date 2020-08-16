package com.firestartermc.dungeons.lobby.commands;

import com.firestartermc.dungeons.lobby.DungeonsLobby;
import com.firestartermc.dungeons.shared.Static;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.firestartermc.kerosene.util.message.Message;

import java.util.List;

public class ReloadCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("firestarter.dungeons.reload")) {
            commandSender.sendMessage(Message.INSUFFICIENT_PERMISSIONS);
            return true;
        }

        // Reload config
        DungeonsLobby.getInstance().reloadConfig();

        // Reload redis data
        DungeonsLobby.getDungeonManager().syncAllData();

        // Reload npc
        DungeonsLobby.getNpcManager().respawnAll();

        commandSender.sendMessage(Static.PREFIX + "Reloaded dungeons");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
