package net.dblsaiko.qcommon.cfg.core.ref;

import net.dblsaiko.qcommon.cfg.core.api.ref.FloatRef;

import java.util.function.Consumer;
import java.util.function.Supplier;

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
