package net.dblsaiko.qcommon.cfg.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.profiler.Profiler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

import net.dblsaiko.qcommon.cfg.core.net.CvarUpdatePacket;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CvarUpdatePacket.register();

        ClientTickCallback.EVENT.register(client -> {
            Profiler profiler = client.getProfiler();
            profiler.push("command_exec");
            ConfigApi.INSTANCE.resumeScripts();
            profiler.pop();
        });

        ConfigApi.INSTANCE.registerOutput(s -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                player.addChatMessage(new LiteralText(s), false);
            }
        });
    }

}
