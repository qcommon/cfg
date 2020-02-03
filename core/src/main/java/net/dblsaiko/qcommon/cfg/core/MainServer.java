package net.dblsaiko.qcommon.cfg.core;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;

public class MainServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ServerTickCallback.EVENT.register((server) -> ConfigApi.INSTANCE.resumeScripts());
    }

}
