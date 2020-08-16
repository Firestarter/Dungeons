package com.firestartermc.dungeons.lobby.commands;

import com.firestartermc.dungeons.lobby.DungeonsLobby;
import com.firestartermc.dungeons.lobby.commands.base.BaseMultiCommand;
import com.firestartermc.dungeons.lobby.npc.Npc;
import com.firestartermc.dungeons.shared.Static;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.firestartermc.kerosene.util.message.Message;

import java.util.List;

public class NpcCommand extends BaseMultiCommand {

    public NpcCommand() {
        registerSubCommand("create", new NpcCreateCommand());
        registerSubCommand("remove", new NpcRemoveCommand());
        registerSubCommand("reload", new NpcReloadCommand());
        registerSubCommand("move", new NpcMoveCommand());
        registerSubCommand("hide", new NpcHidingCommand());
        registerSubCommand("show", new NpcHidingCommand());
    }

    private static class NpcCreateCommand implements TabExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Static.PREFIX + "This command can only be executed by a player.");
                return true;
            }

            if (!sender.hasPermission("firestarter.dungeons.npc")) {
                sender.sendMessage(Message.INSUFFICIENT_PERMISSIONS);
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage(Static.PREFIX + "Usage: /d npc create <id> <name> <dungeon_id>"); // TODO fix
                return true;
            }

            Player player = (Player) sender;
            String id = args[0];
            String name = args[1];
            String dungeonId = args[2];

            if (DungeonsLobby.getNpcManager().createNpc(id, name, dungeonId, player.getLocation())) {
                sender.sendMessage(Static.PREFIX + "NPC created!");
                return true;
            }

            sender.sendMessage(Static.PREFIX + "Failed to create npc. :(");
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            if (args.length == 3) {
                return ImmutableList.copyOf(DungeonsLobby.getDungeonManager().getDungeonKeys());
            }
            return ImmutableList.of();
        }
    }

    private static class NpcRemoveCommand implements TabExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            if (!sender.hasPermission("firestarter.dungeons.npc")) {
                sender.sendMessage(Message.INSUFFICIENT_PERMISSIONS);
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(Static.PREFIX + "Usage: /d npc move <id>"); // TODO fix
                return true;
            }

            Npc npc = DungeonsLobby.getNpcManager().getNpc(args[0]);
            if (npc == null) {
                sender.sendMessage(Static.PREFIX + "No NPC found with id '" + args[0] + "'."); // TODO change to "error:" prefix
                return true;
            }

            if (DungeonsLobby.getNpcManager().remove(npc)) {
                sender.sendMessage(Static.PREFIX + "NPC removed.");
            } else {
                sender.sendMessage(Static.PREFIX + "Failed to remove NPC. But was hidden and despawned.");
            }
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            return args.length == 1 ? ImmutableList.copyOf(DungeonsLobby.getNpcManager().getNpcIds()) : ImmutableList.of();
        }
    }

    private static class NpcMoveCommand implements TabExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Static.PREFIX + "This command can only be executed by a player.");
                return true;
            }

            if (!sender.hasPermission("firestarter.dungeons.npc")) {
                sender.sendMessage(Message.INSUFFICIENT_PERMISSIONS);
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(Static.PREFIX + "Usage: /d npc move <id>"); // TODO fix
                return true;
            }

            Player player = (Player) sender;
            Npc npc = DungeonsLobby.getNpcManager().getNpc(args[0]);
            if (npc == null) {
                sender.sendMessage(Static.PREFIX + "No NPC found with id '" + args[0] + "'."); // TODO change to "error:" prefix
                return true;
            }

            Location newLocation = player.getLocation();
            npc.move(newLocation);
            sender.sendMessage(Static.PREFIX + "NPC moved.");
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            return args.length == 1 ? ImmutableList.copyOf(DungeonsLobby.getNpcManager().getNpcIds()) : ImmutableList.of();
        }
    }

    private static class NpcReloadCommand implements TabExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            if (!sender.hasPermission("firestarter.dungeons.npc")) {
                sender.sendMessage(Message.INSUFFICIENT_PERMISSIONS);
                return true;
            }

            if (args.length < 1) {
                DungeonsLobby.getNpcManager().respawnAll();
                sender.sendMessage(Static.PREFIX + "Reloaded all NPCs.");
            } else {
                Npc npc = DungeonsLobby.getNpcManager().getNpc(args[0]);
                if (npc == null) {
                    sender.sendMessage(Static.PREFIX + "No NPC found with id '" + args[0] + "'."); // TODO change to "error:" prefix
                    return true;
                }
                npc.respawn();
                sender.sendMessage(Static.PREFIX + "Reloaded NPC '" + npc.getId() + "'.");
            }
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            return args.length == 1 ? ImmutableList.copyOf(DungeonsLobby.getNpcManager().getNpcIds()) : ImmutableList.of();
        }
    }

    private static class NpcHidingCommand implements TabExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if (!sender.hasPermission("firestarter.dungeons.npc")) {
                sender.sendMessage(Message.INSUFFICIENT_PERMISSIONS);
                return true;
            }

            if (args.length < 1) {
                sender.sendMessage(Static.PREFIX + "Usage: /d npc move <id>"); // TODO fix
                return true;
            }

            Npc npc = DungeonsLobby.getNpcManager().getNpc(args[0]);
            if (npc == null) {
                sender.sendMessage(Static.PREFIX + "No NPC found with id '" + args[0] + "'."); // TODO change to "error:" prefix
                return true;
            }

            if (label.equalsIgnoreCase("hide")) {
                sender.sendMessage(Static.PREFIX + (npc.hide() ? "NPC is now hidden." : "NPC is already hidden"));
            } else if (label.equalsIgnoreCase("show")) {
                sender.sendMessage(Static.PREFIX + (npc.show() ? "NPC is now shown." : "NPC is already shown"));
            }
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            return args.length == 1 ? ImmutableList.copyOf(DungeonsLobby.getNpcManager().getNpcIds()) : ImmutableList.of();
        }
    }
}
