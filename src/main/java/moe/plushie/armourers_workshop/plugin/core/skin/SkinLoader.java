package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.IResultHandler;

public class SkinLoader {

    private static final SkinLoader INSTANCE = new SkinLoader();

    public static SkinLoader getInstance() {
        return INSTANCE;
    }

    public Skin loadSkin(String identifier) {
        String type = "";
        String filePath = "";
        if (identifier.equals("ws:/胡桃/胡桃.armour")) {
            filePath = "armourers_workshop/skin-library/胡桃/胡桃.armour";
            type = "armourers:outfit";
        }
        if (identifier.equals("ws:/胡桃/护摩之杖.armour")) {
            filePath = "armourers_workshop/skin-library/胡桃/护摩之杖.armour";
            type = "armourers:sword";
        }
        if (filePath.isEmpty()) {
            return null;
        }
        Skin skin = new Skin();
        skin.path = filePath;
        skin.skinType = type;
        return skin;
    }

    public void loadSkin(String identifier, IResultHandler<Skin> handler) {
        String type = "";
        String filePath = "";
        if (identifier.equals("db:00001")) {
            filePath = "armourers_workshop/skin-library/胡桃/胡桃.armour";
            type = "armourers:outfit";
        }
        if (identifier.equals("db:00002")) {
            filePath = "armourers_workshop/skin-library/胡桃/护摩之杖.armour";
            type = "armourers:sword";
        }
        if (filePath.isEmpty()) {
            handler.reject(new RuntimeException("not implement yet"));
            return;
        }
        Skin skin = new Skin();
        skin.path = filePath;
        skin.skinType = type;
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
