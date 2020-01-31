package net.dblsaiko.qcommon.cfg.core.api;

import jdk.internal.jline.internal.Nullable;
import org.jetbrains.annotations.NotNull;

public interface ConsoleOutput {

    void print(@NotNull String s);

    default void print(@Nullable Object o) {
        print(o != null ? o.toString() : "null");
    }

    default void printf(String format, Object... args) {
        print(String.format(format, args));
    }

}
