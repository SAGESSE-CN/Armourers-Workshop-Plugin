package moe.plushie.armourers_workshop.plugin.api.skin;

import java.io.InputStream;

public interface ISkinLibraryLoader {

    InputStream loadSkin(String skinId) throws Exception;
}
