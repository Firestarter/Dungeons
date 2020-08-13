package com.firestartermc.dungeons.lobby.commands;

import com.firestartermc.dungeons.lobby.DungeonsLobby;
import com.firestartermc.dungeons.lobby.commands.base.BaseMultiCommand;
import com.firestartermc.dungeons.shared.Static;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nkomarn.kerosene.util.message.Message;

import java.util.List;

public class NpcCommand extends BaseMultiCommand {

    public NpcCommand() {
        registerSubCommand("reload", new NpcReloadCommand());
        registerSubCommand("move", new NpcMoveCommand());
        registerSubCommand("hide", new NpcHidingCommand());
        registerSubCommand("show", new NpcHidingCommand());
    }

    private static class NpcMoveCommand implements TabExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Static.PREFIX + "This command can only be executed by a player.");
                return true;
            }

            if (!sender.hasPermission("firestarter.dungeons.npc")) {
                sender.sendMessage(Message.INSUFFICIENT_PERMISSIONS);
                return true;
            }

            Player player = (Player) sender;
            Location newLocation = player.getLocation();
            DungeonsLobby.getNpcManager().move(newLocation);

            sender.sendMessage(Static.PREFIX + "NPC moved.");
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
                sender.sendMessage(Message.INSUFFICIENT_PERMISSIONS);
                return true;
            }

            DungeonsLobby.getNpcManager().reload();

            sender.sendMessage(Static.PREFIX + "Reloaded Dungeon NPC.");
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            return ImmutableList.of();
        }
    }

    private static class NpcHidingCommand implements TabExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
            if (label.equalsIgnoreCase("hide")) {
                if (DungeonsLobby.getNpcManager().hide()) {
                    sender.sendMessage(Static.PREFIX + "NPC is now hidden.");
                } else {
                    sender.sendMessage(Static.PREFIX + "NPC is already hidden");
                }
            } else if (label.equalsIgnoreCase("show")) {
                if (DungeonsLobby.getNpcManager().show()) {
                    sender.sendMessage(Static.PREFIX + "NPC is now shown.");
                } else {
                    sender.sendMessage(Static.PREFIX + "NPC is already shown.");
                }
            }
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
            return null;
        }
    }
}
