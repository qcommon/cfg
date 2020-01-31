package net.dblsaiko.qcommon.cfg.core.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public interface ConsoleOutput {

    /**
     * Print a new message to the log. Adding a newline is not necessary.
     *
     * @param s the string to print
     */
    void print(@NotNull String s);

    /**
     * Print an object to the log, or <code>"null"</code> if the argument is null
     *
     * @param o the object to print
     * @see ConsoleOutput#print(String)
     */
    default void print(@Nullable Object o) {
        print(o != null ? o.toString() : "null");
    }

    /**
     * Print a formatted string to the output.
     *
     * @param format the format
     * @param args   the format arguments
     * @see ConsoleOutput#print(String)
     * @see String#format(String, Object...)
     */
    default void printf(String format, Object... args) {
        print(String.format(format, args));
    }

}
