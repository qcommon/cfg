package net.dblsaiko.qcommon.cfg.keys;

import org.jetbrains.annotations.Nullable;

public interface Key {

    String getName();

    @Nullable
    static Key byName(String s) {
        Key k = KeyboardKey.byName(s);
        if (k != null) return k;
        k = MouseButton.byName(s);
        if (k != null) return k;
        k = MouseWheel.byName(s);
        return k;
    }

}
