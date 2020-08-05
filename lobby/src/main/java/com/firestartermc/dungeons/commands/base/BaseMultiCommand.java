package com.firestartermc.dungeons.commands.base;

import com.firestartermc.dungeons.DungeonsLobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMultiCommand implements TabExecutor {

    private final HashMap<String, TabExecutor> childCommands = new HashMap<>();

    protected void registerSubCommand(String name, TabExecutor executor, String... aliases) {
        childCommands.put(name, executor);
        if (aliases != null && aliases.length > 0) {
            Arrays.stream(aliases).forEach(a -> childCommands.put(a, executor));
        }
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length > 0) {
            if (childCommands.containsKey(args[0])) {
                return childCommands.get(args[0]).onCommand(commandSender, command, alias, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        return onCommand(commandSender, alias, args);
    }

    @Override
    public final @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length > 0) {
            if (childCommands.containsKey(args[0])) {
                return childCommands.get(args[0]).onTabComplete(commandSender, command, alias, Arrays.copyOfRange(args, 1, args.length));
            }
            return childCommands.keySet().stream().filter(s -> s.contains(args[0])).collect(Collectors.toList());
        }
        return onTabComplete(commandSender, alias, args);
    }

    public boolean onCommand(CommandSender sender, String alias, String[] args) {
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return new ArrayList<>(childCommands.keySet());
    }
}
