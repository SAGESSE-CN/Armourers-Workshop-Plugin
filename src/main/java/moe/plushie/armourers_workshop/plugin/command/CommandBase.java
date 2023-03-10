package moe.plushie.armourers_workshop.plugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class CommandBase {
    public abstract void onConsoleCommand(CommandSender sender, String[] args);

    public abstract void onPlayerCommand(Player player, String[] args);

    public abstract String getPermission();

    public abstract String getCommandDesc();

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    public int getLength() {
        return (getCommandDesc().split(" ")).length - 1;
    }

    public String getCommand() {
        return getCommandDesc().split(" ")[1].toLowerCase();
    }

}
