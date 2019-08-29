package therealfarfetchd.commoncfg.client.binds

import org.lwjgl.glfw.GLFW

interface Key {
  val id: String
  val sortIndex: Int

  companion object {
    fun byId(id: String): Key? {
      return KeyboardKey.byId(id) as Key?
             ?: MouseKey.byId(id)
             ?: MouseWheelKey.byId(id)
    }
  }
}

enum class MouseKey(override val id: String) : Key {
  MOUSE1("mouse1"),
  MOUSE2("mouse2"),
  MOUSE3("mouse3"),
  MOUSE4("mouse4"),
  MOUSE5("mouse5");

  override val sortIndex: Int = ordinal + 1000

  companion object {
    private val id2key = values().associateBy { it.id }
    private val idx2key = values()

    fun byId(id: String) = id2key[id]
    fun byButton(idx: Int) = idx2key.getOrNull(idx)
  }
}

enum class MouseWheelKey(override val id: String) : Key {
  MOUSE_WHEEL_UP("mouse_wheel_up"),
  MOUSE_WHEEL_DOWN("mouse_wheel_down"),
  MOUSE_WHEEL_LEFT("mouse_wheel_left"),
  MOUSE_WHEEL_RIGHT("mouse_wheel_right");

  override val sortIndex: Int = ordinal + 2000

  companion object {
    private val id2key = values().associateBy { it.id }

    fun byId(id: String) = id2key[id]

    fun byVDirection(dir: Int) = when {
      dir < 0 -> MOUSE_WHEEL_DOWN
      dir > 0 -> MOUSE_WHEEL_UP
      else -> null
    }

    fun byHDirection(dir: Int) = when {
      dir < 0 -> MOUSE_WHEEL_LEFT
      dir > 0 -> MOUSE_WHEEL_RIGHT
      else -> null
    }
  }
}

enum class KeyboardKey(val scancode: Int, override val id: String) : Key {
  SPACE(GLFW.GLFW_KEY_SPACE, "space"),
  QUOTE(GLFW.GLFW_KEY_APOSTROPHE, "'"),
  COMMA(GLFW.GLFW_KEY_COMMA, ","),
  MINUS(GLFW.GLFW_KEY_MINUS, "-"),
  DOT(GLFW.GLFW_KEY_PERIOD, "."),
  SLASH(GLFW.GLFW_KEY_SLASH, "/"),
  NUM_0(GLFW.GLFW_KEY_0, "0"),
  NUM_1(GLFW.GLFW_KEY_1, "1"),
  NUM_2(GLFW.GLFW_KEY_2, "2"),
  NUM_3(GLFW.GLFW_KEY_3, "3"),
  NUM_4(GLFW.GLFW_KEY_4, "4"),
  NUM_5(GLFW.GLFW_KEY_5, "5"),
  NUM_6(GLFW.GLFW_KEY_6, "6"),
  NUM_7(GLFW.GLFW_KEY_7, "7"),
  NUM_8(GLFW.GLFW_KEY_8, "8"),
  NUM_9(GLFW.GLFW_KEY_9, "9"),
  SEMICOLON(GLFW.GLFW_KEY_SEMICOLON, ";"),
  EQUALS(GLFW.GLFW_KEY_EQUAL, "="),
  A(GLFW.GLFW_KEY_A, "a"),
  B(GLFW.GLFW_KEY_B, "b"),
  C(GLFW.GLFW_KEY_C, "c"),
  D(GLFW.GLFW_KEY_D, "d"),
  E(GLFW.GLFW_KEY_E, "e"),
  F(GLFW.GLFW_KEY_F, "f"),
  G(GLFW.GLFW_KEY_G, "g"),
  H(GLFW.GLFW_KEY_H, "h"),
  I(GLFW.GLFW_KEY_I, "i"),
  J(GLFW.GLFW_KEY_J, "j"),
  K(GLFW.GLFW_KEY_K, "k"),
  L(GLFW.GLFW_KEY_L, "l"),
  M(GLFW.GLFW_KEY_M, "m"),
  N(GLFW.GLFW_KEY_N, "n"),
  O(GLFW.GLFW_KEY_O, "o"),
  P(GLFW.GLFW_KEY_P, "p"),
  Q(GLFW.GLFW_KEY_Q, "q"),
  R(GLFW.GLFW_KEY_R, "r"),
  S(GLFW.GLFW_KEY_S, "s"),
  T(GLFW.GLFW_KEY_T, "t"),
  U(GLFW.GLFW_KEY_U, "u"),
  V(GLFW.GLFW_KEY_V, "v"),
  W(GLFW.GLFW_KEY_W, "w"),
  X(GLFW.GLFW_KEY_X, "x"),
  Y(GLFW.GLFW_KEY_Y, "y"),
  Z(GLFW.GLFW_KEY_Z, "z"),
  LBRACKET(GLFW.GLFW_KEY_LEFT_BRACKET, "["),
  BACKSLASH(GLFW.GLFW_KEY_BACKSLASH, "\\"),
  RBRACKET(GLFW.GLFW_KEY_RIGHT_BRACKET, "]"),
  BACKTICK(GLFW.GLFW_KEY_GRAVE_ACCENT, "`"),
  ESCAPE(GLFW.GLFW_KEY_ESCAPE, "esc"),
  RETURN(GLFW.GLFW_KEY_ENTER, "return"),
  TAB(GLFW.GLFW_KEY_TAB, "tab"),
  BACKSPACE(GLFW.GLFW_KEY_BACKSPACE, "backspace"),
  INSERT(GLFW.GLFW_KEY_INSERT, "insert"),
  DELETE(GLFW.GLFW_KEY_DELETE, "delete"),
  RIGHTARROW(GLFW.GLFW_KEY_RIGHT, "rightarrow"),
  LEFTARROW(GLFW.GLFW_KEY_LEFT, "leftarrow"),
  DOWNARROW(GLFW.GLFW_KEY_DOWN, "downarrow"),
  UPARROW(GLFW.GLFW_KEY_UP, "uparrow"),
  PAGEUP(GLFW.GLFW_KEY_PAGE_UP, "pageup"),
  PAGEDOWN(GLFW.GLFW_KEY_PAGE_DOWN, "pagedown"),
  HOME(GLFW.GLFW_KEY_HOME, "home"),
  END(GLFW.GLFW_KEY_END, "end"),
  CAPSLOCK(GLFW.GLFW_KEY_CAPS_LOCK, "capslock"),
  SCROLLLOCK(GLFW.GLFW_KEY_SCROLL_LOCK, "scrolllock"),
  NUMLOCK(GLFW.GLFW_KEY_NUM_LOCK, "numlock"),
  PRINT(GLFW.GLFW_KEY_PRINT_SCREEN, "print"),
  PAUSE(GLFW.GLFW_KEY_PAUSE, "pause"),
  F1(GLFW.GLFW_KEY_F1, "f1"),
  F2(GLFW.GLFW_KEY_F2, "f2"),
  F3(GLFW.GLFW_KEY_F3, "f3"),
  F4(GLFW.GLFW_KEY_F4, "f4"),
  F5(GLFW.GLFW_KEY_F5, "f5"),
  F6(GLFW.GLFW_KEY_F6, "f6"),
  F7(GLFW.GLFW_KEY_F7, "f7"),
  F8(GLFW.GLFW_KEY_F8, "f8"),
  F9(GLFW.GLFW_KEY_F9, "f9"),
  F10(GLFW.GLFW_KEY_F10, "f10"),
  F11(GLFW.GLFW_KEY_F11, "f11"),
  F12(GLFW.GLFW_KEY_F12, "f12"),
  F13(GLFW.GLFW_KEY_F13, "f13"),
  F14(GLFW.GLFW_KEY_F14, "f14"),
  F15(GLFW.GLFW_KEY_F15, "f15"),
  F16(GLFW.GLFW_KEY_F16, "f16"),
  F17(GLFW.GLFW_KEY_F17, "f17"),
  F18(GLFW.GLFW_KEY_F18, "f18"),
  F19(GLFW.GLFW_KEY_F19, "f19"),
  F20(GLFW.GLFW_KEY_F20, "f20"),
  F21(GLFW.GLFW_KEY_F21, "f21"),
  F22(GLFW.GLFW_KEY_F22, "f22"),
  F23(GLFW.GLFW_KEY_F23, "f23"),
  F24(GLFW.GLFW_KEY_F24, "f24"),
  F25(GLFW.GLFW_KEY_F25, "f25"),
  KP_0(GLFW.GLFW_KEY_KP_0, "kp_0"),
  KP_1(GLFW.GLFW_KEY_KP_1, "kp_1"),
  KP_2(GLFW.GLFW_KEY_KP_2, "kp_2"),
  KP_3(GLFW.GLFW_KEY_KP_3, "kp_3"),
  KP_4(GLFW.GLFW_KEY_KP_4, "kp_4"),
  KP_5(GLFW.GLFW_KEY_KP_5, "kp_5"),
  KP_6(GLFW.GLFW_KEY_KP_6, "kp_6"),
  KP_7(GLFW.GLFW_KEY_KP_7, "kp_7"),
  KP_8(GLFW.GLFW_KEY_KP_8, "kp_8"),
  KP_9(GLFW.GLFW_KEY_KP_9, "kp_9"),
  KP_DECIMAL(GLFW.GLFW_KEY_KP_DECIMAL, "kp_decimal"),
  KP_DIVIDE(GLFW.GLFW_KEY_KP_DIVIDE, "kp_divide"),
  KP_MULTIPLY(GLFW.GLFW_KEY_KP_MULTIPLY, "kp_multiply"),
  KP_SUBTRACT(GLFW.GLFW_KEY_KP_SUBTRACT, "kp_subtract"),
  KP_ADD(GLFW.GLFW_KEY_KP_ADD, "kp_add"),
  KP_ENTER(GLFW.GLFW_KEY_KP_ENTER, "kp_enter"),
  KP_EQUAL(GLFW.GLFW_KEY_KP_EQUAL, "kp_equal"),
  SHIFT(GLFW.GLFW_KEY_LEFT_SHIFT, "shift"),
  CTRL(GLFW.GLFW_KEY_LEFT_CONTROL, "ctrl"),
  ALT(GLFW.GLFW_KEY_LEFT_ALT, "alt"),
  META(GLFW.GLFW_KEY_LEFT_SUPER, "meta"),
  RSHIFT(GLFW.GLFW_KEY_RIGHT_SHIFT, "rshift"),
  RCTRL(GLFW.GLFW_KEY_RIGHT_CONTROL, "rctrl"),
  RALT(GLFW.GLFW_KEY_RIGHT_ALT, "ralt"),
  RMETA(GLFW.GLFW_KEY_RIGHT_SUPER, "rmeta"),
  MENU(GLFW.GLFW_KEY_MENU, "menu");

  override val sortIndex: Int = scancode

  companion object {
    private val id2key = values().associateBy { it.id }
    private val scancode2key = values().associateBy { it.scancode }

    fun byId(id: String) = id2key[id]
    fun byCode(scancode: Int) = scancode2key[scancode]
  }
}