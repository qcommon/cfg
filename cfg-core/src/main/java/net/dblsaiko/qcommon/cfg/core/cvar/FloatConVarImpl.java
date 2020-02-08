package net.dblsaiko.qcommon.cfg.core.cvar;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.cvar.FloatConVar;
import net.dblsaiko.qcommon.cfg.core.api.ref.FloatRef;

import static net.dblsaiko.qcommon.cfg.core.util.ArrayUtil.arrayOf;

public abstract class FloatConVarImpl implements FloatConVar {

    private final float defaultValue;
    private final Float min;
    private final Float max;
    private final Float step;

    protected FloatConVarImpl(float defaultValue, FloatConVarImpl.Options options) {
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
        return arrayOf(Float.toString(get()));
    }

    @Override
    @NotNull
    public String getStringRepr() {
        return Float.toString(get());
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

    public static class Owned extends FloatConVarImpl {
        private float value;

        public Owned(float defaultValue, FloatConVarImpl.Options options) {
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

    public static class Wrapped extends FloatConVarImpl {

        private FloatRef ref;

        public Wrapped(FloatRef ref, float defaultValue, FloatConVarImpl.Options options) {
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
