package net.dblsaiko.qcommon.cfg.keys.mixin;

import net.dblsaiko.qcommon.cfg.keys.binding.BindManager;
import net.dblsaiko.qcommon.cfg.keys.binding.MouseWheel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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

    /**
     * @author 2xsaiko
     */
    @Overwrite
    private void onMouseScroll(long windowHandle, double xoffset, double yoffset) {
        if (windowHandle != client.getWindow().getHandle()) return;
        if (client.overlay != null) return;
        double f = (client.options.discreteMouseScroll ? signum(yoffset) : yoffset) * client.options.mouseWheelSensitivity;

        if (client.currentScreen != null) {
            Window window = client.getWindow();
            double g = getX() * window.getScaledWidth() / window.getWidth();
            double h = getY() * window.getScaledHeight() / window.getHeight();
            client.currentScreen.mouseScrolled(g, h, f);
            return;
        }

        if (signum(f) != signum(eventDeltaWheel)) eventDeltaWheel = 0.0;

        eventDeltaWheel += f % 1;
        int f1 = (int) f;
        MouseWheel key = MouseWheel.byVDirection(f1);
        if (key == null) return;
        for (int i = 0; i < abs(f1); i++) {
            BindManager.INSTANCE.onKeyPressed(key);
            BindManager.INSTANCE.onKeyReleased(key);
        }
    }

}
