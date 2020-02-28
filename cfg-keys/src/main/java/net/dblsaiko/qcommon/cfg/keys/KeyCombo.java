package net.dblsaiko.qcommon.cfg.keys;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class KeyCombo {

    private final Set<KeyboardKey> keys;

    public KeyCombo(Set<KeyboardKey> keys) {
        this.keys = Collections.unmodifiableSet(new HashSet<>(keys));
    }

    public Set<KeyboardKey> getKeys() {
        return keys;
    }

}
