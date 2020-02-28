package net.dblsaiko.qcommon.cfg.core.api.cvar;

import net.dblsaiko.qcommon.cfg.core.api.ref.BoolRef;
import net.dblsaiko.qcommon.cfg.core.cvar.BoolConVarImpl;
import net.dblsaiko.qcommon.cfg.core.cvar.BoolConVarOptions;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ConVar} storing a boolean.
 */
public interface BoolConVar extends ConVar {

    /**
     * Get the value stored in this cvar.
     *
     * @return the current value
     */
    boolean get();

    /**
     * Set the value stored in this cvar.
     * The value will be clamped to the min and max values if they exist.
     *
     * @param value the value to set this cvar to
     */
    void set(boolean value);

    /**
     * Create a new {@link BoolConVar} that owns its value.
     *
     * @param defaultValue the default and initial value for this ConVar
     * @return the new {@link BoolConVar}
     */
    @NotNull
    static BoolConVar owned(boolean defaultValue) {
        return owned(defaultValue, BoolConVar.Options.create());
    }

    /**
     * Create a new {@link IntConVar} that owns its value.
     *
     * @param defaultValue the default and initial value for this ConVar
     * @param options      the options used for the ConVar
     * @return the new {@link IntConVar}
     */
    @NotNull
    static BoolConVar owned(boolean defaultValue, @NotNull BoolConVar.Options options) {
        return new BoolConVarImpl.Owned(defaultValue, options);
    }

    /**
     * Create a new {@link BoolConVar} delegating to an {@link BoolRef}.
     * The default value will be set to the current value of the passed reference.
     *
     * @param ref the reference
     * @return the new {@link BoolConVar}
     */
    @NotNull
    static BoolConVar wrap(BoolRef ref) {
        return wrap(ref, ref.get());
    }

    /**
     * Create a new {@link BoolConVar} delegating to an {@link BoolRef}.
     *
     * @param ref          the reference
     * @param defaultValue the default value for this ConVar
     * @return the new {@link BoolConVar}
     */
    @NotNull
    static BoolConVar wrap(@NotNull BoolRef ref, boolean defaultValue) {
        return wrap(ref, defaultValue, BoolConVar.Options.create());
    }

    /**
     * Create a new {@link BoolConVar} delegating to an {@link BoolRef}.
     *
     * @param ref          the reference
     * @param defaultValue the default value for this ConVar
     * @param options      the options used for the ConVar
     * @return the new {@link BoolConVar}
     */
    @NotNull
    static BoolConVar wrap(@NotNull BoolRef ref, boolean defaultValue, @NotNull BoolConVar.Options options) {
        return new BoolConVarImpl.Wrapped(ref, defaultValue, options);
    }

    /**
     * Options for creating a {@link BoolConVar}.
     */
    interface Options {

        @NotNull
        BoolConVar.Options style(BoolConVar.Style style);

        /**
         * Create a new {@link BoolConVar.Options} instance with default settings
         * (NONZERO style)
         *
         * @return a new {@link BoolConVar.Options} instance
         */
        @NotNull
        static BoolConVar.Options create() {
            return BoolConVarOptions.create();
        }

    }

    /**
     * The style a {@link BoolConVar} should use for serialization and display.
     */
    enum Style {
        /**
         * Any integer except for '0' will be treated as <code>true</code>.
         * Serialization will use '1' to represent <code>true</code> and '0' to represent <code>false</code>.
         */
        NONZERO,

        /**
         * 'yes' will be treated as <code>true</code>, everything else as <code>false</code>.
         * Serialization will use 'yes' to represent <code>true</code> and 'no' to represent <code>false</code>.
         */
        YES_NO,

        /**
         * 'true' will be treated as <code>true</code>, everything else as <code>false</code>.
         * Serialization will use 'true' to represent <code>true</code> and 'false' to represent <code>false</code>.
         */
        TRUE_FALSE,
    }

}
