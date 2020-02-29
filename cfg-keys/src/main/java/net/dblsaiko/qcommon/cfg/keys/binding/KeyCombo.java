package net.dblsaiko.qcommon.cfg.keys.binding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class KeyCombo {

    private final Set<Key> keys;

    public KeyCombo(Set<Key> keys) {
        this.keys = Collections.unmodifiableSet(new HashSet<>(keys));
    }

    public Set<Key> getKeys() {
        return keys;
    }

    public String asString() {
        return keys.stream()
            .map(Key::getName)
            .sorted()
            .collect(Collectors.joining("+"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyCombo keyCombo = (KeyCombo) o;
        return Objects.equals(keys, keyCombo.keys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keys);
    }

    @Override
    public String toString() {
        return asString();
    }

    @Nullable
    public static KeyCombo parse(@NotNull String string) {
        List<String> parts = new ArrayList<>();
        int nextSep;
        while ((nextSep = string.indexOf('+', 1)) != -1) {
            parts.add(string.substring(0, nextSep));
            string = string.substring(nextSep + 1);
        }
        parts.add(string);
        Set<Key> collect = parts.stream().map(Key::byName).collect(Collectors.toSet());
        if (collect.contains(null)) return null;
        return new KeyCombo(collect);
    }

}
