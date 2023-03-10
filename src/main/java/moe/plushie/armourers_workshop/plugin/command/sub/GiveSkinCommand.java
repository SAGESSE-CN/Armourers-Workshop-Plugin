package moe.plushie.armourers_workshop.plugin.command.sub;


import moe.plushie.armourers_workshop.plugin.command.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveSkinCommand extends CommandBase {

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        sender.sendMessage("do something");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "armourers.admin";
    }

    @Override
    public int getLength() {
        return 3;
    }

    @Override
    public String getCommandDesc() {
        return "/armourers giveSkin [player] [armourersFile]";
    }

}
