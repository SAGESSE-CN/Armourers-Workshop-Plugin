package moe.plushie.armourers_workshop.customapi.version;

import moe.plushie.armourers_workshop.customapi.CustomAPI;

public class Versions {

    public static final CustomAPI API = new VersionMatcher().match();
}
