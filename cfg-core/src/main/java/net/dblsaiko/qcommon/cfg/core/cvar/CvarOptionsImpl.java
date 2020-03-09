package net.dblsaiko.qcommon.cfg.core.cvar;

import net.dblsaiko.qcommon.cfg.core.api.CommandDescription;
import net.dblsaiko.qcommon.cfg.core.api.cvar.CvarOptions;
import net.dblsaiko.qcommon.cfg.core.cmd.CommandOptionsImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CvarOptionsImpl implements CvarOptions {

    private final boolean sync;
    private final String savedTo;
    private final CommandDescription desc;
    private final CommandDescription extendedDesc;

    private CvarOptionsImpl(boolean sync, String savedTo, CommandDescription desc, CommandDescription extendedDesc) {
        this.sync = sync;
        this.savedTo = savedTo;
        this.desc = desc;
        this.extendedDesc = extendedDesc;
    }

    @NotNull
    @Override
    public CvarOptionsImpl sync() {
        return new CvarOptionsImpl(true, savedTo, desc, extendedDesc);
    }

    @NotNull
    @Override
    public CvarOptionsImpl save(@NotNull String file) {
        return new CvarOptionsImpl(sync, file, desc, extendedDesc);
    }

    @Override
    public CvarOptions desc(@NotNull CommandDescription description) {
        return new CvarOptionsImpl(sync, savedTo, description, extendedDesc);
    }

    @Override
    public CvarOptions extendedDesc(@NotNull CommandDescription description) {
        return new CvarOptionsImpl(sync, savedTo, desc, description);
    }

    public boolean isSync() {
        return sync;
    }

    @Nullable
    public String getSavedTo() {
        return savedTo;
    }

    public CommandDescription getDesc() {
        return desc;
    }

    public CommandDescription getExtendedDesc() {
        return extendedDesc;
    }

    public static CvarOptionsImpl create() {
        return new CvarOptionsImpl(false, null, CommandOptionsImpl.DEFAULT_DESC, CommandOptionsImpl.DEFAULT_EXT_DESC);
    }

}
