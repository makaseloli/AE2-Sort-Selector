package io.github.makaseloli.ae2sortselector;

import io.github.makaseloli.ae2sortselector.config.ModConfigs;
import io.github.makaseloli.ae2sortselector.mdk.config.PlatformConfigRegistrar;
import io.github.makaseloli.ae2sortselector.mdk.config.VersionedConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MODID)
public class ModMain {
    public ModMain(FMLJavaModLoadingContext ctx) {
        Constants.LOGGER.debug(Constants.INITIALIZING, ModUtils.loc("1.20.1-forge"));
        PlatformConfigRegistrar.registerAll(ctx, VersionedConfigSpec.bindAll(ModConfigs.ALL));
    }
}
