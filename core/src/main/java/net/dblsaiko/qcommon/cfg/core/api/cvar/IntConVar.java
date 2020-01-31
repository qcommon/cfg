package net.dblsaiko.qcommon.cfg.core.api.cvar;

import java.util.OptionalInt;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.impl.cvar.IntConVar.Owned;
import net.dblsaiko.qcommon.cfg.core.api.impl.cvar.IntConVar.Wrapped;
import net.dblsaiko.qcommon.cfg.core.api.impl.cvar.IntConVarOptions;
import net.dblsaiko.qcommon.cfg.core.api.ref.IntRef;

public interface IntConVar extends ConVar {
    int get();

    void set(int value);

    @NotNull
    OptionalInt min();

    @NotNull
    OptionalInt max();

    @NotNull
    static IntConVar owned(int defaultValue) {
        return owned(defaultValue, OptionalInt.empty(), OptionalInt.empty());
    }

    @NotNull
    static IntConVar owned(int defaultValue, OptionalInt min, OptionalInt max) {
        return new Owned(defaultValue, min, max);
    }

    @NotNull
    static IntConVar wrap(IntRef ref) {
        return wrap(ref, ref.get());
    }

    @NotNull
    static IntConVar wrap(IntRef ref, int defaultValue) {
        return wrap(ref, defaultValue, OptionalInt.empty(), OptionalInt.empty());
    }

    @NotNull
    static IntConVar wrap(IntRef ref, int defaultValue, OptionalInt min, OptionalInt max) {
        return new Wrapped(ref, defaultValue, min, max);
    }

    interface Options {

        @NotNull
        Options min(int value);

        @NotNull
        Options max(int value);

        @NotNull
        static Options create() {
            return new IntConVarOptions();
        }

    }

}
