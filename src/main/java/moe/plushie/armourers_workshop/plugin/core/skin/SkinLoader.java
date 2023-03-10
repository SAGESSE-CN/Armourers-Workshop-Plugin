package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.IResultHandler;
import moe.plushie.armourers_workshop.plugin.api.skin.ISkinType;

public class SkinLoader {

    private static final SkinLoader INSTANCE = new SkinLoader();

    public static SkinLoader getInstance() {
        return INSTANCE;
    }

    public Skin loadSkin(String identifier) {
        ISkinType type = null;
        String filePath = "";
        if (identifier.equals("ws:/胡桃/胡桃.armour")) {
            filePath = "armourers_workshop/skin-library/胡桃/胡桃.armour";
            type = SkinTypes.OUTFIT;
        }
        if (identifier.equals("ws:/胡桃/护摩之杖.armour")) {
            filePath = "armourers_workshop/skin-library/胡桃/护摩之杖.armour";
            type = SkinTypes.ITEM_SWORD;
        }
        if (filePath.isEmpty()) {
            return null;
        }
        Skin skin = new Skin();
        skin.path = filePath;
        skin.type = type;
        return skin;
    }

    public void loadSkin(String identifier, IResultHandler<Skin> handler) {
        ISkinType type = null;
        String filePath = "";
        if (identifier.equals("db:00001")) {
            filePath = "armourers_workshop/skin-library/胡桃/胡桃.armour";
            type = SkinTypes.OUTFIT;
        }
        if (identifier.equals("db:00002")) {
            filePath = "armourers_workshop/skin-library/胡桃/护摩之杖.armour";
            type = SkinTypes.ITEM_SWORD;
        }
        if (filePath.isEmpty()) {
            handler.reject(new RuntimeException("not implement yet"));
            return;
        }
        Skin skin = new Skin();
        skin.path = filePath;
        skin.type = type;
        handler.accept(skin);
    }

    public String saveSkin(String identifier, Skin skin) {
        if (identifier.startsWith("db:")) {
            return identifier;
        }
        // TODO: IMP
        // copy file to skin-database
        if (identifier.equals("ws:/胡桃/胡桃.armour")) {
            identifier = "db:00001";
        }
        if (identifier.equals("ws:/胡桃/护摩之杖.armour")) {
            identifier = "db:00002";
        }
        return identifier;
    }
}
