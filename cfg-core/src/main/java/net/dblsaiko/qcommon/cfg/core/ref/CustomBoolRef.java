package net.dblsaiko.qcommon.cfg.core.ref;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.dblsaiko.qcommon.cfg.core.api.ref.BoolRef;

import java.util.function.BooleanSupplier;

public class CustomBoolRef implements BoolRef {

    private final BooleanSupplier getter;
    private final BooleanConsumer setter;

    public CustomBoolRef(BooleanSupplier getter, BooleanConsumer setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public boolean get() {
        return getter.getAsBoolean();
    }

    @Override
    public void set(boolean value) {
        setter.accept(value);
    }

}
