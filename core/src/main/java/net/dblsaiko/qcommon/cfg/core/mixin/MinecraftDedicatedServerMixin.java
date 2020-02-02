package net.dblsaiko.qcommon.cfg.core.mixin;

import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.dblsaiko.qcommon.cfg.core.api.impl.ConfigApi;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin {

    @Inject(method = "setupServer()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeNano()J", ordinal = 1))
    private void init(CallbackInfoReturnable<Boolean> cir) {
        ConfigApi.INSTANCE.onLoad();
    }

    @Inject(method = "shutdown()V", at = @At(value = "RETURN"))
    private void shutdown(CallbackInfo ci) {
        ConfigApi.INSTANCE.saveConfig();
    }

    @Inject(
        method = "enqueueCommand(Ljava/lang/String;Lnet/minecraft/server/command/ServerCommandSource;)V",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void executeQueuedCommands(String string, ServerCommandSource source, CallbackInfo ci) {
        if (string.startsWith("?")) {
            ConfigApi.INSTANCE.exec(string.substring(1), ExecSource.CONSOLE);
            ci.cancel();
        }
    }

}
