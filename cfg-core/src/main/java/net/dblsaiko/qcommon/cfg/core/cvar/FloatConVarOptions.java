package net.dblsaiko.qcommon.cfg.core.cvar;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.cvar.FloatConVar.Options;

public class FloatConVarOptions implements Options {

    private final Float min;
    private final Float max;
    private final Float step;

    private FloatConVarOptions(Float min, Float max, Float step) {
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @NotNull
    @Override
    public Options min(float value) {
        return new FloatConVarOptions(value, max, step);
    }

    @NotNull
    @Override
    public Options max(float value) {
        return new FloatConVarOptions(min, value, step);
    }

    @NotNull
    @Override
    public Options step(float value) {
        return new FloatConVarOptions(min, max, value);
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

    public static FloatConVarOptions create() {
        return new FloatConVarOptions(null, null, null);
    }

}
