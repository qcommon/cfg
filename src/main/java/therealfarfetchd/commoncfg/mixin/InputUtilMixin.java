package therealfarfetchd.commoncfg.mixin;

import net.minecraft.client.util.InputUtil;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharModsCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import therealfarfetchd.commoncfg.CommonCfg;

@Mixin(InputUtil.class)
public class InputUtilMixin {

    @Inject(method = "setKeyboardCallbacks(JLorg/lwjgl/glfw/GLFWKeyCallbackI;Lorg/lwjgl/glfw/GLFWCharModsCallbackI;)V", at = @At("RETURN"))
    private static void setKeyboardCallbacks(long window, GLFWKeyCallbackI gLFWKeyCallbackI_1, GLFWCharModsCallbackI gLFWCharModsCallbackI_1, CallbackInfo ci) {
        GLFW.glfwSetKeyCallback(window, CommonCfg.INSTANCE.getApi().getIm()::onKey);
    }

}
