package net.dblsaiko.qcommon.cfg.core.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConsoleOutput {

    void print(@NotNull String s);

    default void print(@Nullable Object o) {
        print(o != null ? o.toString() : "null");
    }

    default void printf(String format, Object... args) {
        print(String.format(format, args));
    }

}
