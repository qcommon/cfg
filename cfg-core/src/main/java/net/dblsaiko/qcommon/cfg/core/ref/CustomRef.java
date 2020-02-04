package net.dblsaiko.qcommon.cfg.core.ref;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.dblsaiko.qcommon.cfg.core.api.ref.Ref;

public class CustomRef<T> implements Ref<T> {

    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public CustomRef(Supplier<T> getter, Consumer<T> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public T get() {
        return getter.get();
    }

    @Override
    public void set(T value) {
        setter.accept(value);
    }

}
