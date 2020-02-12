package net.dblsaiko.qcommon.cfg.core.cvar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;

public class CvarOptionsImpl implements CvarOptions {

    private final boolean sync;
    private final String savedTo;

    private CvarOptionsImpl(boolean sync, String savedTo) {
        this.sync = sync;
        this.savedTo = savedTo;
    }

    @NotNull
    @Override
    public CvarOptionsImpl sync() {
        return new CvarOptionsImpl(true, savedTo);
    }

    @NotNull
    @Override
    public CvarOptionsImpl save(@NotNull String file) {
        return new CvarOptionsImpl(sync, file);
    }

    public boolean isSync() {
        return sync;
    }

    @Nullable
    public String getSavedTo() {
        return savedTo;
    }

    public static CvarOptionsImpl create() {
        return new CvarOptionsImpl(false, null);
    }

}
