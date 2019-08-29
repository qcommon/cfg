package therealfarfetchd.commoncfg.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import therealfarfetchd.commoncfg.CommonCfg;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(
        method = "render(F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay()V",
            shift = Shift.AFTER
        )
    )
    private void render(float delta, CallbackInfo ci) {
        // FIXME test code, remove before release
        final String s = CommonCfg.INSTANCE.getApi().getIm().keys();
        final int width = client.textRenderer.getStringWidth(s);
        client.textRenderer.draw(s, client.window.getScaledWidth() - 2f - width, 2f, 0xfa5c61);
    }

}
