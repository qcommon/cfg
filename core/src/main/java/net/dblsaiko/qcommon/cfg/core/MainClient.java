package net.dblsaiko.qcommon.cfg.core;

import net.dblsaiko.qcommon.cfg.core.net.CvarUpdatePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CvarUpdatePacket.register();

        ClientTickCallback.EVENT.register((client) -> ConfigApi.INSTANCE.resumeScripts());

        ConfigApi.INSTANCE.registerOutput(s -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                player.addChatMessage(new LiteralText(s), false);
            }
        });
    }

}
