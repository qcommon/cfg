package net.dblsaiko.qcommon.cfg.core.api.cvar;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.ref.Ref;

/**
 * A {@link ConVar} storing a {@link String}.
 */
public interface StringConVar extends ConVar {

    /**
     * Get the value stored in this cvar.
     *
     * @return the current value
     */
    @NotNull
    String get();

    /**
     * Set the value stored in this cvar.
     * The value will be clamped to the min and max values if they exist.
     *
     * @param value the value to set this cvar to
     */
    void set(@NotNull String value);

    /**
     * Create a new {@link StringConVar} that owns its value.
     *
     * @param defaultValue the default and initial value for this ConVar
     * @return the new {@link StringConVar}
     */
    @NotNull
    static StringConVar owned(String defaultValue) {
        return new net.dblsaiko.qcommon.cfg.core.api.impl.cvar.StringConVar.Owned(defaultValue);
    }

    /**
     * Create a new {@link StringConVar} delegating to a {@link Ref}.
     * The default value will be set to the current value of the passed reference.
     *
     * @param ref the reference
     * @return the new {@link StringConVar}
     */
    @NotNull
    static StringConVar wrap(Ref<String> ref) {
        return wrap(ref, ref.get());
    }

    /**
     * Create a new {@link StringConVar} delegating to a {@link Ref}.
     *
     * @param ref          the reference
     * @param defaultValue the default value for this ConVar
     * @return the new {@link StringConVar}
     */
    @NotNull
    static StringConVar wrap(Ref<String> ref, String defaultValue) {
        return new net.dblsaiko.qcommon.cfg.core.api.impl.cvar.StringConVar.Wrapped(ref, defaultValue);
    }

}
