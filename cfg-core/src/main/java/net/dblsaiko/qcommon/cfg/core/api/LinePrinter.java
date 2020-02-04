package net.dblsaiko.qcommon.cfg.core.api;

import net.dblsaiko.qcommon.cfg.core.api.persistence.PersistenceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A line based printer, used for console output or in {@link PersistenceListener}.
 */
public interface LinePrinter {

    /**
     * Print a new line. If this string contains newlines, act as if print()
     * was called multiple times, once for each line.
     *
     * @param s the string to print
     */
    void print(@NotNull String s);

    /**
     * Print an empty new line.
     */
    default void print() {
        print("");
    }

    /**
     * Print an object, or <code>"null"</code> if the argument is null
     *
     * @param o the object to print
     * @see LinePrinter#print(String)
     */
    default void print(@Nullable Object o) {
        print(o != null ? o.toString() : "null");
    }

    /**
     * Print a formatted string.
     *
     * @param format the format
     * @param args   the format arguments
     * @see LinePrinter#print(String)
     * @see String#format(String, Object...)
     */
    default void printf(String format, Object... args) {
        print(String.format(format, args));
    }

}
