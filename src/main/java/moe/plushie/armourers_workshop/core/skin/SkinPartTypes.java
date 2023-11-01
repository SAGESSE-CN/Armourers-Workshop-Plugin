package moe.plushie.armourers_workshop.core.skin;

import moe.plushie.armourers_workshop.api.skin.ISkinPartType;

public class SkinPartTypes {

    public static final ISkinPartType UNKNOWN = register("unknown");//, new UnknownPartType());

    public static final ISkinPartType BIPPED_HAT = register("hat.base");//, new HatPartType());
    public static final ISkinPartType BIPPED_HEAD = register("head.base");//, new HeadPartType());
    public static final ISkinPartType BIPPED_CHEST = register("chest.base");//, new ChestPartType());
    public static final ISkinPartType BIPPED_LEFT_ARM = register("chest.leftArm");//, new LeftArmPartType());
    public static final ISkinPartType BIPPED_RIGHT_ARM = register("chest.rightArm");//, new RightArmPartType());
    public static final ISkinPartType BIPPED_SKIRT = register("legs.skirt");//, new SkirtPartType());
    public static final ISkinPartType BIPPED_LEFT_LEG = register("legs.leftLeg");//, new LeftLegPartType());
    public static final ISkinPartType BIPPED_RIGHT_LEG = register("legs.rightLeg");//, new RightLegPartType());
    public static final ISkinPartType BIPPED_LEFT_FOOT = register("feet.leftFoot");//, new LeftFootPartType());
    public static final ISkinPartType BIPPED_RIGHT_FOOT = register("feet.rightFoot");//, new RightFootPartType());
    public static final ISkinPartType BIPPED_LEFT_WING = register("wings.leftWing");//, new LeftWingPartType());
    public static final ISkinPartType BIPPED_RIGHT_WING = register("wings.rightWing");//, new RightWingPartType());

    public static final ISkinPartType TOOL_PICKAXE = register("pickaxe.base");//, new ItemPartType());
    public static final ISkinPartType TOOL_AXE = register("axe.base");//, new ItemPartType());
    public static final ISkinPartType TOOL_SHOVEL = register("shovel.base");//, new ItemPartType());
    public static final ISkinPartType TOOL_HOE = register("hoe.base");//, new ItemPartType());

    public static final ISkinPartType ITEM_BOW0 = register("bow.frame0");//, new BowPartType(0));
    public static final ISkinPartType ITEM_BOW1 = register("bow.frame1");//, new BowPartType(1));
    public static final ISkinPartType ITEM_BOW2 = register("bow.frame2");//, new BowPartType(2));
    public static final ISkinPartType ITEM_BOW3 = register("bow.frame3");//, new BowPartType(3));

    public static final ISkinPartType ITEM_ARROW = register("bow.arrow");//, new ArrowPartType());
    public static final ISkinPartType ITEM_SWORD = register("sword.base");//, new ItemPartType());
    public static final ISkinPartType ITEM_SHIELD = register("shield.base");//, new ShieldPartType());
    public static final ISkinPartType ITEM_TRIDENT = register("trident.base");//, new ItemPartType());

    public static final ISkinPartType ITEM = register("item.base");//, new ItemPartType());
    public static final ISkinPartType BLOCK = register("block.base");//, new BlockPartType());
    public static final ISkinPartType BLOCK_MULTI = register("block.multiblock");//, new MultiBlockPartType());

    public static final ISkinPartType ADVANCED = register("part.advanced_part");//, new AdvancedPartType());


    private static ISkinPartType register(String name) {
        return new ISkinPartType() {
        };
    }

//    private static ISkinPartType register(String name, ISkinPartType partType) {
//        return partType;
//    }
}
