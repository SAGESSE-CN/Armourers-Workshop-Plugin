package moe.plushie.armourers_workshop.plugin.init.command.sub;


import moe.plushie.armourers_workshop.plugin.core.data.DataDomain;
import moe.plushie.armourers_workshop.plugin.core.skin.Skin;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinLoader;
import moe.plushie.armourers_workshop.plugin.init.command.CommandBase;
import moe.plushie.armourers_workshop.plugin.utils.BukkitStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class GiveSkinCommand extends CommandBase {

    private SkinDescriptor loadSkinDescriptor(String[] args) {
        String identifier = args[1];//("skin");
        if (identifier.isEmpty()) {
            return SkinDescriptor.EMPTY;
        }
//        ColorScheme scheme = ColorScheme.EMPTY;
//        if (containsNode(context, "dying")) {
//            scheme = ColorSchemeArgumentType.getColorScheme(context, "dying");
//        }
        boolean needCopy = false;
        if (identifier.startsWith("/")) {
            identifier = DataDomain.DEDICATED_SERVER.normalize(identifier);
            needCopy = true; // save the skin to the database
        }
        Skin skin = SkinLoader.getInstance().loadSkin(identifier);
        if (skin != null) {
            if (needCopy) {
                identifier = SkinLoader.getInstance().saveSkin(identifier, skin);
            }
            return new SkinDescriptor(identifier, skin.getType());
        }
        return SkinDescriptor.EMPTY;
    }

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        String playerSelector = args[0];
        SkinDescriptor descriptor = loadSkinDescriptor(args);
        if (descriptor.isEmpty()) {
            return;
        }
        for (Entity entity : Bukkit.selectEntities(sender, playerSelector)) {
            if (entity instanceof Player) {
                BukkitStackUtils.giveItemTo(descriptor.asItemStack(), (Player) entity);
                // context.getSource().sendSuccess(Component.translatable("commands.give.success.single", 1, itemStack.getDisplayName(), player.getDisplayName()), true);
            }
        }
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "armourers_workshop.command.admin";
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
