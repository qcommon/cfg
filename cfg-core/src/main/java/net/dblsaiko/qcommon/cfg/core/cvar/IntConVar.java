package net.dblsaiko.qcommon.cfg.core.cvar;

import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.ref.IntRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.dblsaiko.qcommon.cfg.core.util.ArrayUtil.arrayOf;

public abstract class IntConVar implements net.dblsaiko.qcommon.cfg.core.api.cvar.IntConVar {

    private final int defaultValue;
    private final Integer min;
    private final Integer max;

    protected IntConVar(int defaultValue, Options options) {
        this.defaultValue = defaultValue;
        IntConVarOptions opts = ((IntConVarOptions) options);
        this.min = opts.getMin();
        this.max = opts.getMax();
    }

    @Override
    public void setFromStrings(@NotNull String[] args) {
        try {
            set(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            try {
                set((int) Float.parseFloat(args[0]));
            } catch (NumberFormatException e1) {
                set(0);
            }
        }
    }

    @NotNull
    @Override
    public String[] getAsStrings() {
        return arrayOf(Integer.toString(get()));
    }

    @Override
    @NotNull
    public String getStringRepr() {
        return Integer.toString(get());
    }

    @Override
    public void printState(@NotNull String name, @NotNull LinePrinter output) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = ").append(this.get()).append(" (default ").append(this.defaultValue);
        if (min != null) sb.append(", min ").append(min);
        if (max != null) sb.append(", max ").append(max);
        sb.append(")");
        output.print(sb.toString());
    }

    protected int clampValue(int value) {
        if (min != null) value = Math.max(value, min);
        if (max != null) value = Math.min(value, max);
        return value;
    }

    @Nullable
    @Override
    public Integer min() {
        return min;
    }

    @Nullable
    @Override
    public Integer max() {
        return max;
    }

    public static class Owned extends IntConVar {
        private int value;

        public Owned(int defaultValue, Options options) {
            super(defaultValue, options);
            this.value = defaultValue;
        }

        @Override
        public int get() {
            return value;
        }

        @Override
        public void set(int value) {
            this.value = clampValue(value);
        }
    }

    public static class Wrapped extends IntConVar {

        private IntRef ref;

        public Wrapped(IntRef ref, int defaultValue, Options options) {
            super(defaultValue, options);
            this.ref = ref;
        }

        @Override
        public int get() {
            return clampValue(ref.get());
        }

        @Override
        public void set(int value) {
            ref.set(clampValue(value));
        }
    }

}
