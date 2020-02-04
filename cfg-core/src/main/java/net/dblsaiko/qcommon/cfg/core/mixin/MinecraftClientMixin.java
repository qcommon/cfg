package net.dblsaiko.qcommon.cfg.core.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;

import net.dblsaiko.qcommon.cfg.core.ConfigApi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "<init>(Lnet/minecraft/client/RunArgs;)V", at = @At("RETURN"))
    private void init(RunArgs args, CallbackInfo ci) {
        ConfigApi.INSTANCE.onLoad();
    }

    @Inject(method = "stop()V", at = @At("HEAD"))
    private void stop(CallbackInfo ci) {
        ConfigApi.INSTANCE.saveConfig();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("RETURN"))
    private void disconnect(CallbackInfo ci) {
        ConfigApi.INSTANCE.unlockCvars();
    }

}