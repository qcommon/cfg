package therealfarfetchd.commoncfg.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import org.lwjgl.glfw.GLFWCharModsCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import therealfarfetchd.commoncfg.CommonCfg;
import therealfarfetchd.commoncfg.client.binds.InputManager;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

@Mixin(InputUtil.class)
public class InputUtilMixin {

    @Inject(method = "setKeyboardCallbacks(JLorg/lwjgl/glfw/GLFWKeyCallbackI;Lorg/lwjgl/glfw/GLFWCharModsCallbackI;)V", at = @At("RETURN"))
    private static void setKeyboardCallbacks(long window, GLFWKeyCallbackI gLFWKeyCallbackI_1, GLFWCharModsCallbackI gLFWCharModsCallbackI_1, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        InputManager im = CommonCfg.INSTANCE.getApi().getIm();
        glfwSetKeyCallback(window, (a, b, c, d, e) -> client.execute(() -> im.onKey(a, b, c, d, e)));
    }

    @Inject(method = "setMouseCallbacks(JLorg/lwjgl/glfw/GLFWCursorPosCallbackI;Lorg/lwjgl/glfw/GLFWMouseButtonCallbackI;Lorg/lwjgl/glfw/GLFWScrollCallbackI;)V", at = @At("RETURN"))
    private static void setMouseCallbacks(long l, GLFWCursorPosCallbackI gLFWCursorPosCallbackI, GLFWMouseButtonCallbackI gLFWMouseButtonCallbackI, GLFWScrollCallbackI gLFWScrollCallbackI, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        InputManager im = CommonCfg.INSTANCE.getApi().getIm();
        // glfwSetMouseButtonCallback(l, (a, b, c, d) -> client.execute(() -> im.onMouse(a, b, c, d)));
        glfwSetScrollCallback(l, (a, b, c) -> client.execute(() -> im.onMouseScroll(a, b, c)));
    }

}
