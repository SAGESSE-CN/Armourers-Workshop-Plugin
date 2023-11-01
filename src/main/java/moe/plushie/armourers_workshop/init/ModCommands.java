package moe.plushie.armourers_workshop.init;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import moe.plushie.armourers_workshop.core.data.DataDomain;
import moe.plushie.armourers_workshop.core.skin.Skin;
import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.core.skin.SkinLoader;
import moe.plushie.armourers_workshop.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.init.platform.MenuManager;
import moe.plushie.armourers_workshop.utils.BukkitUtils;
import moe.plushie.armourers_workshop.utils.ObjectUtils;
import net.cocoonmc.Cocoon;
import net.cocoonmc.core.item.ItemStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class ModCommands {

    public static void init() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(Cocoon.getPlugin()).silentLogs(true));

        new CommandTree("armourers")
                .then(literal("setSkin").then(entities().then(slots().then(skins().executes(Executor::setSkin))).then(skins().executes(Executor::setSkin))))
                .then(literal("giveSkin").then(players().then(skins().executes(Executor::giveSkin))))
                .then(literal("rsyncWardrobe").then(players().executes(Executor::resyncWardrobe)))
                .then(literal("openWardrobe").then(entities().executes(Executor::openWardrobe)))
                .register();
    }

    private static Argument<?> literal(String name) {
        return new LiteralArgument(name);
    }

    static Argument<?> players() {
        return new EntitySelectorArgument.ManyPlayers("players");
    }

    static Argument<?> entities() {
        return new EntitySelectorArgument.ManyEntities("entities");
    }

    static Argument<?> slots() {
        return new IntegerArgument("slot", 1, 10);
    }

    static Argument<?> skins() {
        return new GreedyStringArgument("skin").replaceSuggestions(ArgumentSuggestions.strings("\"", "/"));
    }

    public static class Executor {

        public static void setSkin(CommandSender sender, CommandArguments commandArguments) {
            int slotIn;
            String identifier;
            Object[] args = commandArguments.args();
            if (args[1] instanceof Integer) {
                slotIn = (int) args[1];
                identifier = (String) args[2];
            } else {
                slotIn = 0;
                identifier = (String) args[1];
            }
            SkinDescriptor descriptor = loadSkinDescriptor(identifier);
            if (descriptor.isEmpty()) {
                return;
            }
            ItemStack itemStack = descriptor.asItemStack();
            Collection<Entity> entities = ObjectUtils.unsafeCast(args[0]);
            for (Entity entity : entities) {
                SkinWardrobe wardrobe = SkinWardrobe.of(net.cocoonmc.core.world.entity.Entity.of(entity));
                SkinSlotType slotType = SkinSlotType.of(descriptor.getType());
                if (slotType != null && wardrobe != null) {
                    int slot = wardrobe.getFreeSlot(slotType);
                    if (slotIn != 0) {
                        slot = slotIn - 1;
                    }
                    wardrobe.setItem(slotType, slot, itemStack);
                    wardrobe.broadcast();
                }
            }
        }

        public static void giveSkin(CommandSender sender, CommandArguments commandArguments) {
            Object[] args = commandArguments.args();
            SkinDescriptor descriptor = loadSkinDescriptor((String) args[1]);
            if (descriptor.isEmpty()) {
                return;
            }
            Collection<Player> players = ObjectUtils.unsafeCast(args[0]);
            for (Player entity : players) {
                BukkitUtils.giveItemTo(descriptor.asItemStack(), entity);
                // context.getSource().sendSuccess(Component.translatable("commands.give.success.single", 1, itemStack.getDisplayName(), player.getDisplayName()), true);
            }
        }

        static void resyncWardrobe(CommandSender sender, CommandArguments commandArguments) {
            Object[] args = commandArguments.args();
            Collection<Player> players = ObjectUtils.unsafeCast(args[0]);
            for (Player player : players) {
                SkinWardrobe wardrobe = SkinWardrobe.of(net.cocoonmc.core.world.entity.Player.of(player));
                if (wardrobe != null) {
                    wardrobe.broadcast();
                }
            }
        }

        static void openWardrobe(CommandSender sender, CommandArguments commandArguments) {
            if (!(sender instanceof Player)) {
                return;
            }
            Object[] args = commandArguments.args();
            Collection<Entity> entities = ObjectUtils.unsafeCast(args[0]);
            Player player = (Player) sender;
            for (Entity entity : entities) {
                SkinWardrobe wardrobe = SkinWardrobe.of(net.cocoonmc.core.world.entity.Entity.of(entity));
                if (wardrobe != null) {
                    MenuManager.openMenu(ModMenuTypes.WARDROBE_OP, net.cocoonmc.core.world.entity.Player.of(player), wardrobe);
                    break;
                }
            }
        }

        public static SkinDescriptor loadSkinDescriptor(String identifier) {
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
    }
}
