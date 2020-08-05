package com.firestartermc.dungeons.commands;

import com.firestartermc.dungeons.DungeonsLobby;
import com.firestartermc.dungeons.commands.base.BaseMultiCommand;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NpcCommand extends BaseMultiCommand {

    public NpcCommand() {
        registerSubCommand("reload", new NpcReloadCommand());
        registerSubCommand("move", new NpcMoveCommand());
    }

    private static class NpcMoveCommand implements TabExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(DungeonsLobby.PREFIX + "This command can only be executed by a player.");
                return true;
            }

            if (!sender.hasPermission("firestarter.dungeons.npc")) {
                sender.sendMessage(DungeonsLobby.INSUFFICIENT_PERMISSIONS);
                return true;
            }

            Player player = (Player) sender;
            Location newLocation = player.getLocation();
            DungeonsLobby.getNpcManager().move(newLocation);

            sender.sendMessage(DungeonsLobby.PREFIX + "NPC moved.");
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            return null;
        }
    }

    private static class NpcReloadCommand implements TabExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            if (!sender.hasPermission("firestarter.dungeons.npc")) {
                sender.sendMessage(DungeonsLobby.INSUFFICIENT_PERMISSIONS);
                return true;
            }

            DungeonsLobby.getNpcManager().despawn();
            DungeonsLobby.getNpcManager().spawn();

            sender.sendMessage(DungeonsLobby.PREFIX + "Reloaded Dungeon NPC.");
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            return ImmutableList.of();
        }
    }
}
