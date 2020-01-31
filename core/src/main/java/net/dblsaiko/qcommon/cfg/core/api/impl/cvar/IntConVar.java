package net.dblsaiko.qcommon.cfg.core.api.impl.cvar;

import java.util.OptionalInt;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.ConsoleOutput;
import net.dblsaiko.qcommon.cfg.core.api.ref.IntRef;

public abstract class IntConVar implements net.dblsaiko.qcommon.cfg.core.api.cvar.IntConVar {

    private final int defaultValue;
    private final OptionalInt min;
    private final OptionalInt max;

    protected IntConVar(int defaultValue, OptionalInt min, OptionalInt max) {
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
    }

    @Override
    public void setFromString(@NotNull String[] args) {
        try {
            set(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            set(0);
        }
    }

    @Override
    public void printState(@NotNull String name, @NotNull ConsoleOutput output) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = ").append(this.get()).append(" (default ").append(this.defaultValue);
        min().ifPresent(min -> sb.append(", min ").append(min));
        max().ifPresent(min -> sb.append(", max ").append(min));
        sb.append(")");
        output.print(sb.toString());
    }

    protected int clampValue(int value) {
        if (min().isPresent()) value = Math.max(value, min().getAsInt());
        if (max().isPresent()) value = Math.max(value, max().getAsInt());
        return value;
    }

    @NotNull
    @Override
    public OptionalInt min() {
        return min;
    }

    @NotNull
    @Override
    public OptionalInt max() {
        return max;
    }

    public static class Owned extends IntConVar {
        private int value;

        public Owned(int defaultValue, OptionalInt min, OptionalInt max) {
            super(defaultValue, min, max);
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

        public Wrapped(IntRef ref, int defaultValue, OptionalInt min, OptionalInt max) {
            super(defaultValue, min, max);
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
