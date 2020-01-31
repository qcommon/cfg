package net.dblsaiko.qcommon.cfg.core.api.cvar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dblsaiko.qcommon.cfg.core.api.impl.cvar.FloatConVarOptions;
import net.dblsaiko.qcommon.cfg.core.api.ref.FloatRef;

/**
 * A {@link ConVar} storing a float.
 */
public interface FloatConVar extends ConVar {

    /**
     * Get the value stored in this cvar.
     *
     * @return the current value
     */
    float get();

    /**
     * Set the value stored in this cvar.
     * The value will be clamped to the min and max values if they exist.
     *
     * @param value the value to set this cvar to
     */
    void set(float value);

    /**
     * The minimum value this cvar can be set to.
     *
     * @return the minimum value of this cvar, or null if no limit exists
     * @see Options#min(float)
     */
    @Nullable
    Float min();

    /**
     * The maximum value this cvar can be set to.
     *
     * @return the maximum value of this cvar, or null if no limit exists
     * @see Options#max(float)
     */
    @Nullable
    Float max();

    /**
     * The minimum increment for this cvar, relative to 0.
     * <p>
     * For example, if step = 0.25, then the cvar can be set to -0.25, 0, 0.25, 0.5, 0.75, ...
     *
     * @return the step value of this cvar, or null if none exists
     * @see Options#step(float)
     */
    @Nullable
    Float step();

    /**
     * Create a new {@link FloatConVar} that owns its value.
     *
     * @param defaultValue the default and initial value for this ConVar
     * @return the new {@link FloatConVar}
     */
    @NotNull
    static FloatConVar owned(float defaultValue) {
        return owned(defaultValue, Options.create());
    }

    /**
     * Create a new {@link FloatConVar} that owns its value.
     *
     * @param defaultValue the default and initial value for this ConVar
     * @param options      the options used for the ConVar
     * @return the new {@link FloatConVar}
     */
    @NotNull
    static FloatConVar owned(float defaultValue, Options options) {
        return new net.dblsaiko.qcommon.cfg.core.api.impl.cvar.FloatConVar.Owned(defaultValue, options);
    }

    /**
     * Create a new {@link FloatConVar} delegating to an {@link FloatRef}.
     * The default value will be set to the current value of the passed reference.
     *
     * @param ref the reference
     * @return the new {@link FloatConVar}
     */
    @NotNull
    static FloatConVar wrap(@NotNull FloatRef ref) {
        return wrap(ref, ref.get());
    }

    /**
     * Create a new {@link FloatConVar} delegating to an {@link FloatRef}.
     *
     * @param ref          the reference
     * @param defaultValue the default value for this ConVar
     * @return the new {@link FloatConVar}
     */
    @NotNull
    static FloatConVar wrap(@NotNull FloatRef ref, float defaultValue) {
        return wrap(ref, defaultValue, Options.create());
    }

    /**
     * Create a new {@link FloatConVar} delegating to an {@link FloatRef}.
     *
     * @param ref          the reference
     * @param defaultValue the default value for this ConVar
     * @param options      the options used for the ConVar
     * @return the new {@link FloatConVar}
     */
    @NotNull
    static FloatConVar wrap(@NotNull FloatRef ref, float defaultValue, @NotNull Options options) {
        return new net.dblsaiko.qcommon.cfg.core.api.impl.cvar.FloatConVar.Wrapped(ref, defaultValue, options);
    }

    /**
     * Options for creating a {@link FloatConVar}.
     */
    interface Options {

        /**
         * Set the minimum value the created {@link FloatConVar} can become.
         *
         * @param value the minimum value
         * @return this
         * @see FloatConVar#min()
         */
        @NotNull
        Options min(float value);

        /**
         * Set the maximum value the created {@link FloatConVar} can become.
         *
         * @param value the maximum value
         * @return this
         * @see FloatConVar#max()
         */
        @NotNull
        Options max(float value);

        /**
         * Set the minimum increment for this {@link FloatConVar}.
         *
         * @param value the minimum increment
         * @return this
         * @see FloatConVar#step()
         */
        @NotNull
        Options step(float value);

        /**
         * Create a new {@link Options} instance with default settings
         * (no min, no max, no step)
         *
         * @return a new {@link Options} instance
         */
        @NotNull
        static Options create() {
            return new FloatConVarOptions();
        }

    }

}
