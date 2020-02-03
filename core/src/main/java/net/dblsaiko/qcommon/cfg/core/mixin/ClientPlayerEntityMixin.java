package net.dblsaiko.qcommon.cfg.core.mixin;

import net.dblsaiko.qcommon.cfg.core.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.ExecSource;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(
        method = "sendChatMessage(Ljava/lang/String;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void sendChatMessage(String message, CallbackInfo ci) {
        if (message.startsWith("?")) {
            ConfigApi.INSTANCE.exec(message.substring(1), ExecSource.CONSOLE);
            ci.cancel();
        }
    }

}
