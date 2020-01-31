package net.dblsaiko.qcommon.cfg.core.api.impl.ref;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.dblsaiko.qcommon.cfg.core.api.ref.FloatRef;

public class CustomFloatRef implements FloatRef {

    private final Supplier<Float> getter;
    private final Consumer<Float> setter;

    public CustomFloatRef(Supplier<Float> getter, Consumer<Float> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public float get() {
        return getter.get();
    }

    @Override
    public void set(float value) {
        setter.accept(value);
    }

}
