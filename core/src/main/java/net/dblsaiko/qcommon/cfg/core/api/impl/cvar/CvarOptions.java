package net.dblsaiko.qcommon.cfg.core.api.impl.cvar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CvarOptions implements net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions {

    private boolean sync = false;
    private String savedTo = null;

    @NotNull
    @Override
    public CvarOptions sync() {
        sync = true;
        return this;
    }

    @NotNull
    @Override
    public CvarOptions save(@NotNull String file) {
        savedTo = file;
        return this;
    }

    public boolean isSync() {
        return sync;
    }

    @Nullable
    public String getSavedTo() {
        return savedTo;
    }

}
