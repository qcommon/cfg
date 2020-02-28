package net.dblsaiko.qcommon.cfg.base.client;

import net.dblsaiko.qcommon.cfg.base.client.rcon.RconClient;
import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.fabricmc.api.ClientModInitializer;

public class Main implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigApi.Mutable api = ConfigApi.getInstanceMut();

        new RconClient().initialize(api);
        initializeOptions(api);
    }

    private void initializeOptions(ConfigApi.Mutable api) {
        OptionWrappers.register(api);
    }

}
