package moe.plushie.armourers_workshop.plugin.core.skin;

import java.io.InputStream;
import java.net.URL;

public class SkinDownloader {

    public static InputStream downloadPreviewSkin(String skinId) throws Exception {
        String url = String.format("http://plushie.moe/armourers_workshop/get-skin.php?skinid=%s", skinId);
        return new URL(url).openStream();
    }

    public static InputStream downloadSkin(String skinId) throws Exception {
        String url = String.format("http://plushie.moe/armourers_workshop/download-skin.php?skinid=%s&skinFileName=", skinId);
        return new URL(url).openStream();
    }
}
