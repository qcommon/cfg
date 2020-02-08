package net.dblsaiko.qcommon.cfg.core.api.cvar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dblsaiko.qcommon.cfg.core.api.ref.IntRef;
import net.dblsaiko.qcommon.cfg.core.cvar.IntConVarImpl.Owned;
import net.dblsaiko.qcommon.cfg.core.cvar.IntConVarImpl.Wrapped;
import net.dblsaiko.qcommon.cfg.core.cvar.IntConVarOptions;

/**
 * A {@link ConVar} storing an integer.
 */
public interface IntConVar extends ConVar {

    /**
     * Get the value stored in this cvar.
     *
     * @return the current value
     */
    int get();

    /**
     * Set the value stored in this cvar.
     * The value will be clamped to the min and max values if they exist.
     *
     * @param value the value to set this cvar to
     */
    void set(int value);

    /**
     * The minimum value this cvar can be set to.
     *
     * @return the minimum value of this cvar, or null if no limit exists
     * @see Options#min(int)
     */
    @Nullable
    Integer min();

    /**
     * The maximum value this cvar can be set to.
     *
     * @return the maximum value of this cvar, or null if no limit exists
     * @see Options#max(int)
     */
    @Nullable
    Integer max();

    /**
     * Create a new {@link IntConVar} that owns its value.
     *
     * @param defaultValue the default and initial value for this ConVar
     * @return the new {@link IntConVar}
     */
    @NotNull
    static IntConVar owned(int defaultValue) {
        return owned(defaultValue, Options.create());
    }

    /**
     * Create a new {@link IntConVar} that owns its value.
     *
     * @param defaultValue the default and initial value for this ConVar
     * @param options      the options used for the ConVar
     * @return the new {@link IntConVar}
     */
    @NotNull
    static IntConVar owned(int defaultValue, @NotNull Options options) {
        return new Owned(defaultValue, options);
    }

    /**
     * Create a new {@link IntConVar} delegating to an {@link IntRef}.
     * The default value will be set to the current value of the passed reference.
     *
     * @param ref the reference
     * @return the new {@link IntConVar}
     */
    @NotNull
    static IntConVar wrap(IntRef ref) {
        return wrap(ref, ref.get());
    }

    /**
     * Create a new {@link IntConVar} delegating to an {@link IntRef}.
     *
     * @param ref          the reference
     * @param defaultValue the default value for this ConVar
     * @return the new {@link IntConVar}
     */
    @NotNull
    static IntConVar wrap(@NotNull IntRef ref, int defaultValue) {
        return wrap(ref, defaultValue, Options.create());
    }

    /**
     * Create a new {@link IntConVar} delegating to an {@link IntRef}.
     *
     * @param ref          the reference
     * @param defaultValue the default value for this ConVar
     * @param options      the options used for the ConVar
     * @return the new {@link IntConVar}
     */
    @NotNull
    static IntConVar wrap(@NotNull IntRef ref, int defaultValue, @NotNull Options options) {
        return new Wrapped(ref, defaultValue, options);
    }

    /**
     * Options for creating a {@link IntConVar}.
     */
    interface Options {

        /**
         * Set the minimum value the created {@link IntConVar} can become.
         *
         * @param value the minimum value
         * @return this
         * @see IntConVar#min()
         */
        @NotNull
        Options min(int value);

        /**
         * Set the maximum value the created {@link IntConVar} can become.
         *
         * @param value the maximum value
         * @return this
         * @see IntConVar#max()
         */
        @NotNull
        Options max(int value);

        /**
         * Create a new {@link Options} instance with default settings
         * (no min, no max)
         *
         * @return a new {@link Options} instance
         */
        @NotNull
        static Options create() {
            return new IntConVarOptions();
        }

    }

}
