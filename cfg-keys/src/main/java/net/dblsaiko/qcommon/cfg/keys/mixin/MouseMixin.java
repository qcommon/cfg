package net.dblsaiko.qcommon.cfg.keys.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.Window;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.dblsaiko.qcommon.cfg.keys.binding.BindManager;
import net.dblsaiko.qcommon.cfg.keys.binding.MouseWheel;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    private double eventDeltaWheel;

    @Inject(
        method = "onMouseScroll(JDD)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z", ordinal = 0),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void doTheThing(long window, double horizontal, double vertical, CallbackInfo ci, double d, float g) {
        int g1 = (int) g;
        MouseWheel key = MouseWheel.byVDirection(g1);

        if (key != null) {
            for (int i = 0; i < abs(g1); i++) {
                BindManager.INSTANCE.onKeyPressed(key);
                BindManager.INSTANCE.onKeyReleased(key);
            }
        }

        ci.cancel();
    }

}
