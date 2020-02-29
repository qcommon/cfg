package net.dblsaiko.qcommon.cfg.keys.binding;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.IntStream;

public class MouseButton implements Key {

    private static final MouseButton[] buttons = IntStream.rangeClosed(1, 10)
        .mapToObj(MouseButton::new)
        .toArray(MouseButton[]::new);

    private final int buttonIndex;

    private MouseButton(int buttonIndex) {
        this.buttonIndex = buttonIndex;
    }

    @Override
    public String getName() {
        return String.format("mouse%d", buttonIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MouseButton that = (MouseButton) o;
        return buttonIndex == that.buttonIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(buttonIndex);
    }

    @Nullable
    public static MouseButton of(int i) {
        if (i < 1) return null;
        if (i < buttons.length + 1) return buttons[i - 1];
        return new MouseButton(i);
    }

    @Nullable
    public static MouseButton byName(String keyName) {
        if (keyName.startsWith("mouse")) {
            try {
                int i = Integer.parseInt(keyName.substring(5));
                if (i > 0) {
                    return MouseButton.of(i);
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

}