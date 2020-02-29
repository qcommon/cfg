package net.dblsaiko.qcommon.cfg.keys.mixin;

import net.dblsaiko.qcommon.cfg.keys.binding.BindManager;
import net.dblsaiko.qcommon.cfg.keys.binding.KeyboardKey;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private boolean repeatEvents;

    /**
     * @author 2xsaiko
     */
    @Overwrite
    public void onKey(long window, int key, int scancode, int action, int mods) {
        if (window != client.getWindow().getHandle()) return;

        Screen screen = client.currentScreen;

        if (screen != null) {
            boolean[] success = {false};
            Screen.wrapScreenError(() -> {
                switch (action) {
                    case GLFW.GLFW_REPEAT:
                        if (!repeatEvents) break;
                    case GLFW.GLFW_PRESS:
                        success[0] = screen.keyPressed(key, scancode, mods);
                        break;
                    case GLFW.GLFW_RELEASE:
                        success[0] = screen.keyReleased(key, scancode, mods);
                        break;
                }
            }, "keyPressed event handler", screen.getClass().getCanonicalName());
            if (success[0] || !screen.passEvents) return;
        }

        KeyboardKey key1 = KeyboardKey.byGlfwCode(key);
        switch (action) {
            case GLFW.GLFW_PRESS:
                BindManager.INSTANCE.onKeyPressed(key1);
                break;
            case GLFW.GLFW_RELEASE:
                BindManager.INSTANCE.onKeyReleased(key1);
                break;
            case GLFW.GLFW_REPEAT:
                BindManager.INSTANCE.onKeyRepeat(key1);
                break;
        }
    }

}
