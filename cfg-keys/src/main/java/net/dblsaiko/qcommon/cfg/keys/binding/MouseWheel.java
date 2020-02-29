package net.dblsaiko.qcommon.cfg.keys.binding;

import org.jetbrains.annotations.Nullable;

public enum MouseWheel implements Key {
    UP("mouse_wheel_up"),
    DOWN("mouse_wheel_down"),
    LEFT("mouse_wheel_left"),
    RIGHT("mouse_wheel_right");

    private final String name;

    MouseWheel(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Nullable
    public static MouseWheel byName(String s) {
        for (MouseWheel value : values()) {
            if (value.getName().equals(s)) {
                return value;
            }
        }
        return null;
    }

    @Nullable
    public static MouseWheel byVDirection(int y) {
        return y == 0 ? null : y < 0 ? MouseWheel.DOWN : MouseWheel.UP;
    }

    @Nullable
    public static MouseWheel byHDirection(int x) {
        return x == 0 ? null : x < 0 ? MouseWheel.LEFT : MouseWheel.RIGHT;
    }

}
