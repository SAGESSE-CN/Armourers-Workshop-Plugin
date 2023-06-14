package moe.plushie.armourers_workshop.plugin.init;

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
import moe.plushie.armourers_workshop.plugin.ArmourersWorkshop;
import moe.plushie.armourers_workshop.plugin.api.ItemStack;
import moe.plushie.armourers_workshop.plugin.core.data.DataDomain;
import moe.plushie.armourers_workshop.plugin.core.skin.Skin;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinLoader;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.plugin.utils.BukkitUtils;
import moe.plushie.armourers_workshop.plugin.utils.ObjectUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ModCommands {

    public static void init() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(ArmourersWorkshop.INSTANCE).silentLogs(true));

        new CommandTree("armourers")
                .then(literal("setSkin").then(entities().then(slots().then(skins().executes(Executor::setSkin))).then(skins().executes(Executor::setSkin))))
                .then(literal("giveSkin").then(players().then(skins().executes(Executor::giveSkin))))
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
                SkinWardrobe wardrobe = SkinWardrobe.of(entity);
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
