package moe.plushie.armourers_workshop.plugin.init;

import moe.plushie.armourers_workshop.plugin.api.Item;
import moe.plushie.armourers_workshop.plugin.api.Items;
import moe.plushie.armourers_workshop.plugin.core.item.SkinUnlockItem;
import moe.plushie.armourers_workshop.plugin.core.item.WandOfStyleItem;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinSlotType;
import org.bukkit.NamespacedKey;

import java.util.function.Function;

@SuppressWarnings("unused")
public class ModItems {

    public static final Item SKIN = normal(Item::new).build("skin");

    public static final Item BOTTLE = normal(Item::new).build("dye-bottle");
    public static final Item WAND_OF_STYLE = normal(WandOfStyleItem::new).build("wand-of-style");

    public static final Item SKIN_UNLOCK_HEAD = unlock(SkinSlotType.HEAD).build("skin-unlock-head");
    public static final Item SKIN_UNLOCK_CHEST = unlock(SkinSlotType.CHEST).build("skin-unlock-chest");
    public static final Item SKIN_UNLOCK_FEET = unlock(SkinSlotType.FEET).build("skin-unlock-feet");
    public static final Item SKIN_UNLOCK_LEGS = unlock(SkinSlotType.LEGS).build("skin-unlock-legs");
    public static final Item SKIN_UNLOCK_WINGS = unlock(SkinSlotType.WINGS).build("skin-unlock-wings");
    public static final Item SKIN_UNLOCK_OUTFIT = unlock(SkinSlotType.OUTFIT).build("skin-unlock-outfit");

    public static void init() {
    }

    private static Builder normal(Function<Item.Properties, Item> factory) {
        return new Builder(factory);
    }

    private static Builder unlock(SkinSlotType slotType) {
        return normal(properties -> new SkinUnlockItem(slotType, properties)).stacksTo(16);
    }

    private static class Builder {

        final Item.Properties properties;
        final Function<Item.Properties, Item> factory;

        public Builder(Function<Item.Properties, Item> factory) {
            this.factory = factory;
            this.properties = new Item.Properties();
        }

        public Builder stacksTo(int i) {
            properties.stacksTo(i);
            return this;
        }

        public Item build(String name) {
            Item item = factory.apply(properties);
            Items.register(new NamespacedKey("armourers_workshop", name), item);
            return item;
        }
    }
}
