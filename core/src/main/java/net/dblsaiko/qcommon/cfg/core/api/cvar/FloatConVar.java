package net.dblsaiko.qcommon.cfg.core.api.cvar;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.impl.cvar.FloatConVarOptions;
import net.dblsaiko.qcommon.cfg.core.api.ref.FloatRef;

public interface FloatConVar extends ConVar {

    float get();

    void set(float value);

    @NotNull
    Optional<Float> min();

    @NotNull
    Optional<Float> max();

    @NotNull
    Optional<Float> step();

    @NotNull
    static FloatConVar owned(float defaultValue) {
        return owned(defaultValue, Options.create());
    }

    @NotNull
    static FloatConVar owned(float defaultValue, Options options) {
        return new net.dblsaiko.qcommon.cfg.core.api.impl.cvar.FloatConVar.Owned(defaultValue, options);
    }

    @NotNull
    static FloatConVar wrap(@NotNull FloatRef ref) {
        return wrap(ref, ref.get());
    }

    @NotNull
    static FloatConVar wrap(@NotNull FloatRef ref, float defaultValue) {
        return wrap(ref, defaultValue, Options.create());
    }

    @NotNull
    static FloatConVar wrap(@NotNull FloatRef ref, float defaultValue, @NotNull Options options) {
        return new net.dblsaiko.qcommon.cfg.core.api.impl.cvar.FloatConVar.Wrapped(ref, defaultValue, options);
    }

    interface Options {

        @NotNull
        Options min(float value);

        @NotNull
        Options max(float value);

        @NotNull
        Options step(float value);

        @NotNull
        static Options create() {
            return new FloatConVarOptions();
        }

    }

}
