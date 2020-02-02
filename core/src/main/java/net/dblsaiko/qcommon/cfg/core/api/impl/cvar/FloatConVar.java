package net.dblsaiko.qcommon.cfg.core.api.impl.cvar;

import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.ref.FloatRef;
import org.jetbrains.annotations.NotNull;

public abstract class FloatConVar implements net.dblsaiko.qcommon.cfg.core.api.cvar.FloatConVar {

    private final float defaultValue;
    private final Float min;
    private final Float max;
    private final Float step;

    protected FloatConVar(float defaultValue, FloatConVar.Options options) {
        this.defaultValue = defaultValue;
        FloatConVarOptions opts = ((FloatConVarOptions) options);
        this.min = opts.getMin();
        this.max = opts.getMax();
        this.step = opts.getStep();
    }

    @Override
    public void setFromStrings(@NotNull String[] args) {
        try {
            set(Float.parseFloat(args[0]));
        } catch (NumberFormatException e) {
            set(0);
        }
    }

    @NotNull
    @Override
    public String[] getAsStrings() {
        return new String[]{Float.toString(get())};
    }

    @Override
    public void printState(@NotNull String name, @NotNull LinePrinter output) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = ").append(this.get()).append(" (default ").append(this.defaultValue);
        if (min != null) sb.append(", min ").append(min);
        if (max != null) sb.append(", max ").append(max);
        if (step != null) sb.append(", step ").append(step);
        sb.append(")");
        output.print(sb.toString());
    }

    protected float clampValue(float value) {
        if (step != null) value = (int) (value / step) * step;
        if (min != null) value = Math.max(value, min);
        if (max != null) value = Math.min(value, max);
        return value;
    }

    @NotNull
    @Override
    public Float min() {
        return min;
    }

    @NotNull
    @Override
    public Float max() {
        return max;
    }

    @NotNull
    @Override
    public Float step() {
        return step;
    }

    public static class Owned extends FloatConVar {
        private float value;

        public Owned(float defaultValue, FloatConVar.Options options) {
            super(defaultValue, options);
            this.value = defaultValue;
        }

        @Override
        public float get() {
            return value;
        }

        @Override
        public void set(float value) {
            this.value = clampValue(value);
        }
    }

    public static class Wrapped extends FloatConVar {

        private FloatRef ref;

        public Wrapped(FloatRef ref, float defaultValue, FloatConVar.Options options) {
            super(defaultValue, options);
            this.ref = ref;
        }

        @Override
        public float get() {
            return clampValue(ref.get());
        }

        @Override
        public void set(float value) {
            ref.set(clampValue(value));
        }
    }

}
