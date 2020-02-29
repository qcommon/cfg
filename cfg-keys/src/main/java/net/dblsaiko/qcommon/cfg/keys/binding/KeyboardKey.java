package net.dblsaiko.qcommon.cfg.keys.binding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum KeyboardKey implements Key {

    SPACE(32, "space"),
    APOSTROPHE(39, "'"),
    COMMA(44, ","),
    MINUS(45, "-"),
    PERIOD(46, "."),
    SLASH(47, "/"),
    NO0(48, "0"),
    NO1(49, "1"),
    NO2(50, "2"),
    NO3(51, "3"),
    NO4(52, "4"),
    NO5(53, "5"),
    NO6(54, "6"),
    NO7(55, "7"),
    NO8(56, "8"),
    NO9(57, "9"),
    SEMICOLON(59, ";"),
    EQUAL(61, "="),
    A(65, "a"),
    B(66, "b"),
    C(67, "c"),
    D(68, "d"),
    E(69, "e"),
    F(70, "f"),
    G(71, "g"),
    H(72, "h"),
    I(73, "i"),
    J(74, "j"),
    K(75, "k"),
    L(76, "l"),
    M(77, "m"),
    N(78, "n"),
    O(79, "o"),
    P(80, "p"),
    Q(81, "q"),
    R(82, "r"),
    S(83, "s"),
    T(84, "t"),
    U(85, "u"),
    V(86, "v"),
    W(87, "w"),
    X(88, "x"),
    Y(89, "y"),
    Z(90, "z"),
    LEFT_BRACKET(91, "["),
    BACKSLASH(92, "\\"),
    RIGHT_BRACKET(93, "]"),
    GRAVE_ACCENT(96, "`"),
    WORLD_1(161, "world_1"),
    WORLD_2(162, "world_2"),
    ESCAPE(256, "esc"),
    ENTER(257, "enter"),
    TAB(258, "tab"),
    BACKSPACE(259, "back"),
    INSERT(260, "insert"),
    DELETE(261, "delete"),
    RIGHT(262, "right"),
    LEFT(263, "left"),
    DOWN(264, "down"),
    UP(265, "up"),
    PAGE_UP(266, "page_up"),
    PAGE_DOWN(267, "page_down"),
    HOME(268, "home"),
    END(269, "end"),
    CAPS_LOCK(280, "caps_lock"),
    SCROLL_LOCK(281, "scroll_lock"),
    NUM_LOCK(282, "num_lock"),
    PRINT_SCREEN(283, "print_screen"),
    PAUSE(284, "pause"),
    F1(290, "f1"),
    F2(291, "f2"),
    F3(292, "f3"),
    F4(293, "f4"),
    F5(294, "f5"),
    F6(295, "f6"),
    F7(296, "f7"),
    F8(297, "f8"),
    F9(298, "f9"),
    F10(299, "f10"),
    F11(300, "f11"),
    F12(301, "f12"),
    F13(302, "f13"),
    F14(303, "f14"),
    F15(304, "f15"),
    F16(305, "f16"),
    F17(306, "f17"),
    F18(307, "f18"),
    F19(308, "f19"),
    F20(309, "f20"),
    F21(310, "f21"),
    F22(311, "f22"),
    F23(312, "f23"),
    F24(313, "f24"),
    F25(314, "f25"),
    KP_0(320, "kp_0"),
    KP_1(321, "kp_1"),
    KP_2(322, "kp_2"),
    KP_3(323, "kp_3"),
    KP_4(324, "kp_4"),
    KP_5(325, "kp_5"),
    KP_6(326, "kp_6"),
    KP_7(327, "kp_7"),
    KP_8(328, "kp_8"),
    KP_9(329, "kp_9"),
    KP_DECIMAL(330, "kp_decimal"),
    KP_DIVIDE(331, "kp_divide"),
    KP_MULTIPLY(332, "kp_multiply"),
    KP_SUBTRACT(333, "kp_subtract"),
    KP_ADD(334, "kp_add"),
    KP_ENTER(335, "kp_enter"),
    KP_EQUAL(336, "kp_equal"),
    LEFT_SHIFT(340, "shift"),
    LEFT_CONTROL(341, "ctrl"),
    LEFT_ALT(342, "alt"),
    LEFT_SUPER(343, "super"),
    RIGHT_SHIFT(344, "rshift"),
    RIGHT_CONTROL(345, "rctrl"),
    RIGHT_ALT(346, "ralt"),
    RIGHT_SUPER(347, "rsuper"),
    MENU(348, "menu");

    private static final Map<String, KeyboardKey> byName = new HashMap<>();
    private static final KeyboardKey[] byId = new KeyboardKey[349];

    private final int glfwCode;
    private final String name;

    KeyboardKey(int glfwCode, String name) {
        this.glfwCode = glfwCode;
        this.name = name;
    }

    public int getGlfwCode() {
        return glfwCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Nullable
    public static KeyboardKey byName(@NotNull String name) {
        return byName.get(name);
    }

    @Nullable
    public static KeyboardKey byGlfwCode(int id) {
        if (id < 0 || id >= byId.length) return null;
        return byId[id];
    }

    static {
        for (KeyboardKey value : values()) {
            byName.put(value.getName(), value);
            byId[value.getGlfwCode()] = value;
        }
    }

}
