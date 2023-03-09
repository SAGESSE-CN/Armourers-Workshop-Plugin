package moe.plushie.armourers_workshop.plugin.core.skin;

import moe.plushie.armourers_workshop.plugin.api.IResultHandler;

public class SkinLoader {

    private static final SkinLoader INSTANCE = new SkinLoader();

    public static SkinLoader getInstance() {
        return INSTANCE;
    }

    public void loadSkin(String identifier, IResultHandler<Skin> handler) {
        String filePath = "";
        if (identifier.equals("db:00001")) {
            filePath = "armourers_workshop/skin-library/胡桃/胡桃.armour";
        }
        if (identifier.equals("db:00002")) {
            filePath = "armourers_workshop/skin-library/胡桃/护摩之杖.armour";
        }
        if (filePath.isEmpty()) {
            handler.reject(new RuntimeException("not implement yet"));
            return;
        }
        Skin skin = new Skin();
        skin.path = filePath;
        handler.accept(skin);
    }
}
