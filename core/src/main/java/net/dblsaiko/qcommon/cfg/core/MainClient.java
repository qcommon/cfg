package net.dblsaiko.qcommon.cfg.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

import net.dblsaiko.qcommon.cfg.core.api.impl.ConfigApi;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickCallback.EVENT.register((client) -> ConfigApi.INSTANCE.resumeScripts());
    }

}
