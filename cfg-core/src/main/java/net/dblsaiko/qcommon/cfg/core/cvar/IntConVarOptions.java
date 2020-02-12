package net.dblsaiko.qcommon.cfg.core.cvar;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.cvar.IntConVar.Options;

public class IntConVarOptions implements Options {

    private final Integer min;
    private final Integer max;

    private IntConVarOptions(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    @NotNull
    @Override
    public Options min(int value) {
        return new IntConVarOptions(value, max);
    }

    @NotNull
    @Override
    public Options max(int value) {
        return new IntConVarOptions(min, value);
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    public static IntConVarOptions create() {
        return new IntConVarOptions(null, null);
    }

}
