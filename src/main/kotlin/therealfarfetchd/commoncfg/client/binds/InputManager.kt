package therealfarfetchd.commoncfg.client.binds

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import org.lwjgl.glfw.GLFW
import therealfarfetchd.commoncfg.client.ext.repeatEvents

class InputManager(private val bm: BindsManager) {

  private val state = mutableSetOf<Key>()

  fun keys() = state.sortedBy { it.sortIndex }.joinToString { it.id }

  fun onKeyDown(key: Key) {
    val mc = MinecraftClient.getInstance()
    val screen = mc.currentScreen

    state += key

    if (screen == null || screen.passEvents) {
      bm.recalculatePressedKeys(state)
    }
  }

  fun onKeyUp(key: Key) {
    val mc = MinecraftClient.getInstance()
    val screen = mc.currentScreen

    state -= key

    if (screen == null || screen.passEvents) {
      bm.recalculatePressedKeys(state)
    }
  }

  fun onKey(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
    val mc = MinecraftClient.getInstance()

    if (window != mc.window.handle) return

    val screen = mc.currentScreen

    if (screen != null) {
      var success = false
      Screen.wrapScreenError({
        success = when {
          action == GLFW.GLFW_PRESS ||
          action == GLFW.GLFW_REPEAT && mc.keyboard.repeatEvents ->
            screen.keyPressed(key, scancode, mods)
          action == GLFW.GLFW_RELEASE ->
            screen.keyReleased(key, scancode, mods)
          else -> false
        }
      }, "keyPressed event handler", screen::class.qualifiedName.orEmpty())
      if (success) return
    }

    when (action) {
      GLFW.GLFW_PRESS -> KeyboardKey.byCode(key)?.also(::onKeyDown)
      GLFW.GLFW_RELEASE -> KeyboardKey.byCode(key)?.also(::onKeyUp)
    }
  }

}
