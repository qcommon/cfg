package net.dblsaiko.qcommon.cfg.keys;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.keys.binding.BindManager;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigApi.Mutable api = ConfigApi.getInstanceMut();
        BindManager.INSTANCE.register(api);
        VanillaKeyWrapper.INSTANCE.register(api);
    }

}
