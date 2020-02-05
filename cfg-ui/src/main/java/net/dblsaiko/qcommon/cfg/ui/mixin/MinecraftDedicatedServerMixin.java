package net.dblsaiko.qcommon.cfg.ui.mixin;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin {

    @Inject(
        method = "enqueueCommand(Ljava/lang/String;Lnet/minecraft/server/command/ServerCommandSource;)V",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void executeQueuedCommands(String string, ServerCommandSource source, CallbackInfo ci) {
        if (string.startsWith("?")) {
            ConfigApi.getInstance().exec(string.substring(1), ExecSource.CONSOLE);
            ci.cancel();
        }
    }

}
