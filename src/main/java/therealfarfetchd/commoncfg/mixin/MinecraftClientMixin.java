package therealfarfetchd.commoncfg.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.world.level.LevelInfo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import therealfarfetchd.commoncfg.CfgLoadTriggersClient;
import therealfarfetchd.commoncfg.CommonCfgClient;
import therealfarfetchd.commoncfg.api.CommonCfgApi;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "<init>(Lnet/minecraft/client/RunArgs;)V", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        CommonCfgClient.INSTANCE.onGameLoaded();
    }

    @Inject(method = "startIntegratedServer(Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/world/level/LevelInfo;)V", at = @At("HEAD"))
    private void startIntegratedServer(String string_1, String string_2, LevelInfo levelInfo_1, CallbackInfo ci) {
        CfgLoadTriggersClient.INSTANCE.onOpenSPWorld();
    }

    @Inject(method = "stop()V", at = @At("HEAD"))
    private void stop(CallbackInfo ci) {
        CommonCfgApi.getInstance().getPersistRegistry().save();
    }

}
