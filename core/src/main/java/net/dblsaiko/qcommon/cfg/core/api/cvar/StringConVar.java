package net.dblsaiko.qcommon.cfg.core.api.cvar;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.ref.Ref;

public interface StringConVar extends ConVar {

    @NotNull
    String get();

    void set(@NotNull String value);

    @NotNull
    static StringConVar owned(String defaultValue) {
        return new net.dblsaiko.qcommon.cfg.core.api.impl.cvar.StringConVar.Owned(defaultValue);
    }

    @NotNull
    static StringConVar wrap(Ref<String> ref) {
        return wrap(ref, ref.get());
    }

    @NotNull
    static StringConVar wrap(Ref<String> ref, String defaultValue) {
        return new net.dblsaiko.qcommon.cfg.core.api.impl.cvar.StringConVar.Wrapped(ref, defaultValue);
    }

}
