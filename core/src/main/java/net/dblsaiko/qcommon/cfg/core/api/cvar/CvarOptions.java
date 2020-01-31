package net.dblsaiko.qcommon.cfg.core.api.cvar;

import org.jetbrains.annotations.NotNull;

public interface CvarOptions {

    @NotNull
    CvarOptions sync();

    @NotNull
    CvarOptions save(@NotNull String file);

    @NotNull
    static CvarOptions create() {
        return new net.dblsaiko.qcommon.cfg.core.api.impl.cvar.CvarOptions();
    }

}
