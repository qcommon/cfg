package therealfarfetchd.commoncfg.binds

import org.lwjgl.glfw.GLFW
import java.util.*

data class Key(val scancode: Int) {

  fun name(): String? {
    return keymap[scancode]
  }

  override fun toString(): String {
    return "Key(scancode=$scancode, name=${name()})"
  }

  companion object {

    fun fromName(name: String): Key? {
      return revmap[name.toLowerCase(Locale.ROOT)]
    }

    private val keymap = mapOf(
      GLFW.GLFW_KEY_SPACE to "space",
      GLFW.GLFW_KEY_APOSTROPHE to "'",
      GLFW.GLFW_KEY_COMMA to ",",
      GLFW.GLFW_KEY_MINUS to "-",
      GLFW.GLFW_KEY_PERIOD to ".",
      GLFW.GLFW_KEY_SLASH to "/",
      GLFW.GLFW_KEY_0 to "0",
      GLFW.GLFW_KEY_1 to "1",
      GLFW.GLFW_KEY_2 to "2",
      GLFW.GLFW_KEY_3 to "3",
      GLFW.GLFW_KEY_4 to "4",
      GLFW.GLFW_KEY_5 to "5",
      GLFW.GLFW_KEY_6 to "6",
      GLFW.GLFW_KEY_7 to "7",
      GLFW.GLFW_KEY_8 to "8",
      GLFW.GLFW_KEY_9 to "9",
      GLFW.GLFW_KEY_SEMICOLON to ";",
      GLFW.GLFW_KEY_EQUAL to "=",
      GLFW.GLFW_KEY_A to "a",
      GLFW.GLFW_KEY_B to "b",
      GLFW.GLFW_KEY_C to "c",
      GLFW.GLFW_KEY_D to "d",
      GLFW.GLFW_KEY_E to "e",
      GLFW.GLFW_KEY_F to "f",
      GLFW.GLFW_KEY_G to "g",
      GLFW.GLFW_KEY_H to "h",
      GLFW.GLFW_KEY_I to "i",
      GLFW.GLFW_KEY_J to "j",
      GLFW.GLFW_KEY_K to "k",
      GLFW.GLFW_KEY_L to "l",
      GLFW.GLFW_KEY_M to "m",
      GLFW.GLFW_KEY_N to "n",
      GLFW.GLFW_KEY_O to "o",
      GLFW.GLFW_KEY_P to "p",
      GLFW.GLFW_KEY_Q to "q",
      GLFW.GLFW_KEY_R to "r",
      GLFW.GLFW_KEY_S to "s",
      GLFW.GLFW_KEY_T to "t",
      GLFW.GLFW_KEY_U to "u",
      GLFW.GLFW_KEY_V to "v",
      GLFW.GLFW_KEY_W to "w",
      GLFW.GLFW_KEY_X to "x",
      GLFW.GLFW_KEY_Y to "y",
      GLFW.GLFW_KEY_Z to "z",
      GLFW.GLFW_KEY_LEFT_BRACKET to "[",
      GLFW.GLFW_KEY_BACKSLASH to "\\",
      GLFW.GLFW_KEY_RIGHT_BRACKET to "]",
      GLFW.GLFW_KEY_GRAVE_ACCENT to "`",
      GLFW.GLFW_KEY_ESCAPE to "esc",
      GLFW.GLFW_KEY_ENTER to "return",
      GLFW.GLFW_KEY_TAB to "tab",
      GLFW.GLFW_KEY_BACKSPACE to "backspace",
      GLFW.GLFW_KEY_INSERT to "insert",
      GLFW.GLFW_KEY_DELETE to "delete",
      GLFW.GLFW_KEY_RIGHT to "rightarrow",
      GLFW.GLFW_KEY_LEFT to "leftarrow",
      GLFW.GLFW_KEY_DOWN to "downarrow",
      GLFW.GLFW_KEY_UP to "uparrow",
      GLFW.GLFW_KEY_PAGE_UP to "pageup",
      GLFW.GLFW_KEY_PAGE_DOWN to "pagedown",
      GLFW.GLFW_KEY_HOME to "home",
      GLFW.GLFW_KEY_END to "end",
      GLFW.GLFW_KEY_CAPS_LOCK to "capslock",
      GLFW.GLFW_KEY_SCROLL_LOCK to "scrolllock",
      GLFW.GLFW_KEY_NUM_LOCK to "numlock",
      GLFW.GLFW_KEY_PRINT_SCREEN to "print",
      GLFW.GLFW_KEY_PAUSE to "pause",
      GLFW.GLFW_KEY_F1 to "f1",
      GLFW.GLFW_KEY_F2 to "f2",
      GLFW.GLFW_KEY_F3 to "f3",
      GLFW.GLFW_KEY_F4 to "f4",
      GLFW.GLFW_KEY_F5 to "f5",
      GLFW.GLFW_KEY_F6 to "f6",
      GLFW.GLFW_KEY_F7 to "f7",
      GLFW.GLFW_KEY_F8 to "f8",
      GLFW.GLFW_KEY_F9 to "f9",
      GLFW.GLFW_KEY_F10 to "f10",
      GLFW.GLFW_KEY_F11 to "f11",
      GLFW.GLFW_KEY_F12 to "f12",
      GLFW.GLFW_KEY_F13 to "f13",
      GLFW.GLFW_KEY_F14 to "f14",
      GLFW.GLFW_KEY_F15 to "f15",
      GLFW.GLFW_KEY_F16 to "f16",
      GLFW.GLFW_KEY_F17 to "f17",
      GLFW.GLFW_KEY_F18 to "f18",
      GLFW.GLFW_KEY_F19 to "f19",
      GLFW.GLFW_KEY_F20 to "f20",
      GLFW.GLFW_KEY_F21 to "f21",
      GLFW.GLFW_KEY_F22 to "f22",
      GLFW.GLFW_KEY_F23 to "f23",
      GLFW.GLFW_KEY_F24 to "f24",
      GLFW.GLFW_KEY_F25 to "f25",
      GLFW.GLFW_KEY_KP_0 to "kp_0",
      GLFW.GLFW_KEY_KP_1 to "kp_1",
      GLFW.GLFW_KEY_KP_2 to "kp_2",
      GLFW.GLFW_KEY_KP_3 to "kp_3",
      GLFW.GLFW_KEY_KP_4 to "kp_4",
      GLFW.GLFW_KEY_KP_5 to "kp_5",
      GLFW.GLFW_KEY_KP_6 to "kp_6",
      GLFW.GLFW_KEY_KP_7 to "kp_7",
      GLFW.GLFW_KEY_KP_8 to "kp_8",
      GLFW.GLFW_KEY_KP_9 to "kp_9",
      GLFW.GLFW_KEY_KP_DECIMAL to "kp_decimal",
      GLFW.GLFW_KEY_KP_DIVIDE to "kp_divide",
      GLFW.GLFW_KEY_KP_MULTIPLY to "kp_multiply",
      GLFW.GLFW_KEY_KP_SUBTRACT to "kp_subtract",
      GLFW.GLFW_KEY_KP_ADD to "kp_add",
      GLFW.GLFW_KEY_KP_ENTER to "kp_enter",
      GLFW.GLFW_KEY_KP_EQUAL to "kp_equal",
      GLFW.GLFW_KEY_LEFT_SHIFT to "shift",
      GLFW.GLFW_KEY_LEFT_CONTROL to "ctrl",
      GLFW.GLFW_KEY_LEFT_ALT to "alt",
      GLFW.GLFW_KEY_LEFT_SUPER to "meta",
      GLFW.GLFW_KEY_RIGHT_SHIFT to "rshift",
      GLFW.GLFW_KEY_RIGHT_CONTROL to "rctrl",
      GLFW.GLFW_KEY_RIGHT_ALT to "ralt",
      GLFW.GLFW_KEY_RIGHT_SUPER to "rmeta",
      GLFW.GLFW_KEY_MENU to "menu"
    )

    private val revmap = keymap.entries.associate { (k, v) -> Pair(v.toLowerCase(Locale.ROOT), Key(k)) }

  }

}