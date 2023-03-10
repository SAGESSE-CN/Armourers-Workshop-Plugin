package moe.plushie.armourers_workshop.plugin.command;

import com.google.common.collect.Maps;
import moe.plushie.armourers_workshop.plugin.command.sub.GiveSkinCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainCommand implements TabExecutor {

    private Map<String, CommandBase> commandMap;

    public MainCommand() {
        this.commandMap = Maps.newHashMap();
        registerCommand(new GiveSkinCommand());
    }

    private void registerCommand(CommandBase commandBase) {
        this.commandMap.put(commandBase.getCommand(), commandBase);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            CommandBase commandBase = this.commandMap.get(args[0].toLowerCase());
            if (commandBase == null) {
                sender.sendMessage("§cUse §f/armourers§c for help.");
            } else if (commandBase.getPermission() != null && !sender.hasPermission(commandBase.getPermission())) {
                sender.sendMessage("§cI'm sorry, but you do not have permission to perform this command.");
            } else if (commandBase.getLength() > args.length) {
                sender.sendMessage("§cUsage: " + commandBase.getCommandDesc());
            } else {
                String[] args1 = Arrays.copyOfRange(args, 1, args.length);
                if (sender instanceof Player) {
                    commandBase.onPlayerCommand((Player) sender, args1);
                } else {
                    commandBase.onConsoleCommand(sender, args1);
                }
            }
        } else {
            sender.sendMessage("/armourers giveSkin [player] [armourersFile] §7| §a给玩家添加时装");
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return this.commandMap.keySet().stream().filter(cmd -> cmd.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        if (args.length >= 2) {
            return this.commandMap.containsKey(args[0].toLowerCase()) ? this.commandMap.get(args[0].toLowerCase()).onTabComplete(sender, command, label, args) : null;
        }
        return null;
    }

}
