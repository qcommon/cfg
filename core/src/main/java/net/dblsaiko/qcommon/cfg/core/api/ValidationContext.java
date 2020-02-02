package net.dblsaiko.qcommon.cfg.core.api;

/**
 * A validation context. This is used to allow for non-exception-based error
 * handling, so that multiple errors can be collected and displayed at once,
 * instead of exceptions that bring the program down immediately before other
 * errors can be noticed.
 */
public interface ValidationContext {

    /**
     * Add a warning to the message queue.
     *
     * @param fmt  the format string
     * @param args the formatting arguments
     * @see String#format(String, Object...)
     */
    void warning(String fmt, Object... args);

    /**
     * Add an error to the message queue.
     * This is critical and will not allow loading to continue.
     *
     * @param fmt  the format string
     * @param args the formatting arguments
     * @see String#format(String, Object...)
     */
    void error(String fmt, Object... args);

}
