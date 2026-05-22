package io.github.makaseloli.ae2sortselector.client;

import io.github.makaseloli.ae2sortselector.Constants;
import io.github.makaseloli.ae2sortselector.client.mdk.config.KeyedConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Constants.MODID, dist = Dist.CLIENT)
public class ModClient {
    public ModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (mod, parent) ->
                new ConfigurationScreen(mod, parent, KeyedConfigScreen::new)
        );
    }
}
