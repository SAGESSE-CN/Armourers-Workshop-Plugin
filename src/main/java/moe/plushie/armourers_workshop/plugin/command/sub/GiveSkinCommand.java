package moe.plushie.armourers_workshop.plugin.command.sub;


import moe.plushie.armourers_workshop.plugin.command.CommandBase;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.plugin.utils.BukkitStackUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class GiveSkinCommand extends CommandBase {

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        String playerUUID = args[0];
        String filePath = args[1];

        // SkinDescriptor descriptor = new SkinDescriptor("db:00001", "armourers:outfit");
        //  BukkitStackUtils.unwrap(descriptor.asItemStack());

        // wardrobe.setItem(SkinSlotType.OUTFIT, 0, new SkinDescriptor("db:00001", "armourers:outfit").asItemStack());
        // wardrobe.setItem(SkinSlotType.SWORD, 0, new SkinDescriptor("db:00002", "armourers:sword").asItemStack());


//        sender.sendMessage("do something");
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
