package therealfarfetchd.commoncfg.binds

import net.minecraft.client.MinecraftClient
import org.lwjgl.glfw.GLFW

class InputManager(private val bm: BindsManager) {

  private val state = mutableSetOf<Key>()

  fun onKeyDown(key: Key) {
    println("Pressed $key")
    state += key
    bm.recalculatePressedKeys(state)
  }

  fun onKeyUp(key: Key) {
    println("Released $key")
    state -= key
    bm.recalculatePressedKeys(state)
  }

  fun onKey(window: Long, key: Int, _scancode: Int, action: Int, _mods: Int) {
    if (window != MinecraftClient.getInstance().window.handle) return
    when (action) {
      GLFW.GLFW_PRESS -> onKeyDown(Key(key))
      GLFW.GLFW_RELEASE -> onKeyUp(Key(key))
    }
  }

}
