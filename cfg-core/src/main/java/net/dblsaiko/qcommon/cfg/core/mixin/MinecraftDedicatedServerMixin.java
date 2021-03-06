package net.dblsaiko.qcommon.cfg.core.mixin;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.dblsaiko.qcommon.cfg.core.ConfigApiImpl;

@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerMixin {

    @Inject(method = "setupServer()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeNano()J", ordinal = 1))
    private void init(CallbackInfoReturnable<Boolean> cir) {
        ConfigApiImpl.INSTANCE.onLoad();
    }

    @Inject(method = "shutdown()V", at = @At(value = "RETURN"))
    private void shutdown(CallbackInfo ci) {
        ConfigApiImpl.INSTANCE.saveConfig();
    }

}
