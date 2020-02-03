package net.dblsaiko.qcommon.cfg.core.ref;

import net.dblsaiko.qcommon.cfg.core.api.ref.IntRef;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class CustomIntRef implements IntRef {

    private final IntSupplier getter;
    private final IntConsumer setter;

    public CustomIntRef(IntSupplier getter, IntConsumer setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public int get() {
        return getter.getAsInt();
    }

    @Override
    public void set(int value) {
        setter.accept(value);
    }

}
