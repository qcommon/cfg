package net.dblsaiko.qcommon.cfg.core.cvar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;

public class CvarOptionsImpl implements CvarOptions {

    private boolean sync = false;
    private String savedTo = null;

    @NotNull
    @Override
    public CvarOptionsImpl sync() {
        sync = true;
        return this;
    }

    @NotNull
    @Override
    public CvarOptionsImpl save(@NotNull String file) {
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
