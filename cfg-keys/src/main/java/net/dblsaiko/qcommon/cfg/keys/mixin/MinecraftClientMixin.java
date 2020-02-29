package net.dblsaiko.qcommon.cfg.keys.mixin;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.keys.VanillaKeyWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(RunArgs args, CallbackInfo ci) {
        VanillaKeyWrapper.INSTANCE.registerWrapCommands(ConfigApi.getInstanceMut());
    }

}
