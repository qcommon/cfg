package net.dblsaiko.qcommon.cfg.core.api.impl.cvar;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.ConsoleOutput;
import net.dblsaiko.qcommon.cfg.core.api.ref.Ref;

public abstract class StringConVar implements net.dblsaiko.qcommon.cfg.core.api.cvar.StringConVar {

    private final String defaultValue;

    protected StringConVar(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void setFromString(@NotNull String[] args) {
        set(args[0]);
    }

    @Override
    public void printState(@NotNull String name, @NotNull ConsoleOutput output) {
        output.printf("%s = '%s' (default '%s')", name, this.get(), this.defaultValue);
    }

    public static class Owned extends StringConVar {
        private String value;

        public Owned(String defaultValue) {
            super(defaultValue);
            this.value = defaultValue;
        }

        @NotNull
        @Override
        public String get() {
            return value;
        }

        @Override
        public void set(@NotNull String value) {
            this.value = value;
        }
    }

    public static class Wrapped extends StringConVar {

        private Ref<String> ref;

        public Wrapped(Ref<String> ref, String defaultValue) {
            super(defaultValue);
            this.ref = ref;
        }

        @NotNull
        @Override
        public String get() {
            return ref.get();
        }

        @Override
        public void set(@NotNull String value) {
            ref.set(value);
        }
    }

}
