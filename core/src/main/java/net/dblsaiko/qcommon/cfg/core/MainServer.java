package net.dblsaiko.qcommon.cfg.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;

import net.dblsaiko.qcommon.cfg.core.api.impl.ConfigApi;

public class MainServer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ServerTickCallback.EVENT.register((server) -> ConfigApi.INSTANCE.resumeScripts());
    }

}
