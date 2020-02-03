package net.dblsaiko.qcommon.cfg.core.cvar;

import net.dblsaiko.qcommon.cfg.core.api.cvar.IntConVar.Options;
import org.jetbrains.annotations.NotNull;

public class IntConVarOptions implements Options {

    private Integer max = null;
    private Integer min = null;

    @NotNull
    @Override
    public Options min(int value) {
        this.min = value;
        return this;
    }

    @NotNull
    @Override
    public Options max(int value) {
        this.max = value;
        return this;
    }

    public Integer getMax() {
        return max;
    }

    public Integer getMin() {
        return min;
    }

}
