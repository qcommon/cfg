package net.dblsaiko.qcommon.cfg.core.cvar;

import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.LinePrinter;
import net.dblsaiko.qcommon.cfg.core.api.cvar.BoolConVar;
import net.dblsaiko.qcommon.cfg.core.api.ref.BoolRef;
import org.jetbrains.annotations.NotNull;

import static net.dblsaiko.qcommon.cfg.core.util.ArrayUtil.arrayOf;

public abstract class BoolConVarImpl implements BoolConVar {

    private final boolean defaultValue;
    private final Style style;

    protected BoolConVarImpl(boolean defaultValue, Options options) {
        this.defaultValue = defaultValue;
        BoolConVarOptions opts = ((BoolConVarOptions) options);
        this.style = opts.getStyle();
    }

    @Override
    public void setFromStrings(@NotNull String[] args) {
        set(parse(args[0]));
    }

    @NotNull
    @Override
    public String[] getAsStrings() {
        return arrayOf(toString(get()));
    }

    @Override
    @NotNull
    public String getStringRepr() {
        return toString(get());
    }

    private boolean parse(String str) {
        switch (style) {
            case NONZERO:
                int i;
                try {
                    i = Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    i = 0;
                }
                return i != 0;
            case YES_NO:
                return "yes".equals(str);
            case TRUE_FALSE:
                return "true".equals(str);
        }
        throw new IllegalStateException("unreachable");
    }

    private String toString(boolean b) {
        String trueValue;
        String falseValue;
        switch (style) {
            case NONZERO:
                trueValue = "1";
                falseValue = "0";
                break;
            case YES_NO:
                trueValue = "yes";
                falseValue = "no";
                break;
            case TRUE_FALSE:
                trueValue = "true";
                falseValue = "false";
                break;
            default:
                throw new IllegalStateException("unreachable");
        }
        return b ? trueValue : falseValue;
    }

    @Override
    public void printState(@NotNull String name, @NotNull LinePrinter output) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = ").append(toString(this.get())).append(" (default ").append(toString(this.defaultValue)).append(")");
        output.print(sb.toString());
        String desc = ConfigApi.getInstance().getDescription(name);
        if (desc != null && !desc.isEmpty()) output.print(desc);
        String longDesc = ConfigApi.getInstance().getLongDescription(name);
        if (longDesc != null && !longDesc.isEmpty()) output.print(longDesc);
    }

    public static class Owned extends BoolConVarImpl {
        private boolean value;

        public Owned(boolean defaultValue, Options options) {
            super(defaultValue, options);
            this.value = defaultValue;
        }

        @Override
        public boolean get() {
            return value;
        }

        @Override
        public void set(boolean value) {
            this.value = value;
        }
    }

    public static class Wrapped extends BoolConVarImpl {

        private BoolRef ref;

        public Wrapped(BoolRef ref, boolean defaultValue, Options options) {
            super(defaultValue, options);
            this.ref = ref;
        }

        @Override
        public boolean get() {
            return ref.get();
        }

        @Override
        public void set(boolean value) {
            ref.set(value);
        }
    }

}
