package net.dblsaiko.qcommon.cfg.core.cvar;

import org.jetbrains.annotations.NotNull;

import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.cvar.StringConVar;
import net.dblsaiko.qcommon.cfg.core.api.ref.Ref;

import static net.dblsaiko.qcommon.cfg.core.util.ArrayUtil.arrayOf;

public abstract class StringConVarImpl implements StringConVar {

    private final String defaultValue;

    protected StringConVarImpl(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void setFromStrings(@NotNull String[] args) {
        set(args[0]);
    }

    @NotNull
    @Override
    public String[] getAsStrings() {
        return arrayOf(get());
    }

    @NotNull
    @Override
    public String getStringRepr() {
        return String.format("'%s'", get());
    }

    @Override
    public void printState(@NotNull String name, @NotNull LinePrinter output) {
        output.printf("%s = '%s' (default '%s')", name, this.get(), this.defaultValue);
    }

    public static class Owned extends StringConVarImpl {
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

    public static class Wrapped extends StringConVarImpl {

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
