package moe.plushie.armourers_workshop;

import com.comphenix.protocol.ProtocolLibrary;
import moe.plushie.armourers_workshop.core.data.DataManager;
import moe.plushie.armourers_workshop.core.data.LocalDataService;
import moe.plushie.armourers_workshop.core.network.NetworkManager;
import moe.plushie.armourers_workshop.core.recipe.SkinningRecipes;
import moe.plushie.armourers_workshop.core.skin.SkinLoader;
import moe.plushie.armourers_workshop.init.ModBlockEntities;
import moe.plushie.armourers_workshop.init.ModBlocks;
import moe.plushie.armourers_workshop.init.ModCommands;
import moe.plushie.armourers_workshop.init.ModConfig;
import moe.plushie.armourers_workshop.init.ModEntityProfiles;
import moe.plushie.armourers_workshop.init.ModItems;
import moe.plushie.armourers_workshop.init.ModMenuTypes;
import moe.plushie.armourers_workshop.init.ModPackets;
import moe.plushie.armourers_workshop.init.handler.EntityEventHandler;
import moe.plushie.armourers_workshop.init.handler.PacketEventHandler;
import moe.plushie.armourers_workshop.library.data.SkinLibraryManager;
import net.cocoonmc.Cocoon;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArmourersWorkshopImpl extends JavaPlugin {

    public static ArmourersWorkshopImpl INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        Cocoon.init(this);

        ModConfig.init();
        ModPackets.init();

        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
        ModMenuTypes.init();
        ModCommands.init();
        ModEntityProfiles.init();

        NetworkManager.init();
        SkinningRecipes.init();
        LocalDataService.start(getServer());
        DataManager.start();
        SkinLoader.getInstance().setup();
        SkinLibraryManager.getServer().start();

        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new EntityEventHandler(), this);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketEventHandler(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SkinLibraryManager.getServer().stop();
        LocalDataService.stop();
        DataManager.stop();
    }
}
