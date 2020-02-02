package net.dblsaiko.qcommon.cfg.core;

import net.dblsaiko.qcommon.cfg.core.api.impl.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.net.CvarUpdatePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CvarUpdatePacket.register();

        ClientTickCallback.EVENT.register((client) -> ConfigApi.INSTANCE.resumeScripts());

        ConfigApi.INSTANCE.registerOutput(s -> {
            if (MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.addChatMessage(new LiteralText(s), false);
            }
        });
    }

}
