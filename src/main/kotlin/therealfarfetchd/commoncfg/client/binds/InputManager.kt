package therealfarfetchd.commoncfg.client.binds

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import org.lwjgl.glfw.GLFW
import therealfarfetchd.commoncfg.client.ext.repeatEvents
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

class InputManager(private val bm: BindsManager) {

  private val state = mutableSetOf<Key>()

  private var dyWheel = 0.0

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

  fun onMouse(window: Long, button: Int, action: Int, mods: Int) {

  }

  fun onMouseScroll(window: Long, xoffset: Double, yoffset: Double) {
    val mc = MinecraftClient.getInstance()
    if (window != mc.window.handle) return
    if (mc.overlay != null) return
    val f = (if (mc.options.discreteMouseScroll) yoffset.sign else yoffset) * mc.options.mouseWheelSensitivity

    mc.currentScreen?.also { screen ->
      val g = mc.mouse.x * mc.getWindow().scaledWidth.toDouble() / mc.getWindow().width.toDouble()
      val h = mc.mouse.y * mc.getWindow().scaledHeight.toDouble() / mc.getWindow().height.toDouble()
      screen.mouseScrolled(g, h, f)
      return
    }

    if (f.sign != dyWheel.sign) dyWheel = 0.0
    dyWheel += f % 1
    val f1 = f.roundToInt()
    val key = MouseWheelKey.byVDirection(f1) ?: return
    repeat(abs(f1)) {
      bm.recalculatePressedKeys(state + key)
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
