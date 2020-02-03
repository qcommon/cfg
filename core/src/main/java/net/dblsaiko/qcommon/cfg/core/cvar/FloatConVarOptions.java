package net.dblsaiko.qcommon.cfg.core.cvar;

import net.dblsaiko.qcommon.cfg.core.api.cvar.FloatConVar.Options;
import org.jetbrains.annotations.NotNull;

public class FloatConVarOptions implements Options {

    private Float max = null;
    private Float min = null;
    private Float step = null;

    @NotNull
    @Override
    public Options min(float value) {
        this.min = value;
        return this;
    }

    @NotNull
    @Override
    public Options max(float value) {
        this.max = value;
        return this;
    }

    @NotNull
    @Override
    public Options step(float value) {
        this.step = value;
        return this;
    }

    public Float getMax() {
        return max;
    }

    public Float getMin() {
        return min;
    }

    public Float getStep() {
        return step;
    }

}
