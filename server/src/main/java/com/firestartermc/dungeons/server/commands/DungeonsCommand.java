package com.firestartermc.dungeons.server.commands;

import com.firestartermc.dungeons.server.DungeonServer;
import com.firestartermc.dungeons.server.commands.base.BaseMultiCommand;
import com.firestartermc.dungeons.server.loader.ServerDungeonInfo;
import com.firestartermc.dungeons.shared.DungeonInfo;
import com.firestartermc.dungeons.shared.Static;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class DungeonsCommand extends BaseMultiCommand {

    public DungeonsCommand() {
        registerSubCommand("scan", new DungeonsScanCommand(), "s");
        registerSubCommand("enable", null);
        registerSubCommand("disable", null);
        registerSubCommand("reload", null);
        registerSubCommand("list", null);
        registerSubCommand("info", null);
    }

    private static final class DungeonsScanCommand implements TabExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            List<ServerDungeonInfo> dungeonInfoList = DungeonServer.getDungeonManager().scanDirectory();

            if (dungeonInfoList.size() == 0) {
                sender.sendMessage(Static.DUNGEON_PREFIX + "No new dungeons found.");
                return true;
            }

            sender.sendMessage("Found: " + dungeonInfoList.stream()
                    .map(ServerDungeonInfo::getId)
                    .collect(Collectors.joining(", ")));
            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            return ImmutableList.of();
        }
    }

}
