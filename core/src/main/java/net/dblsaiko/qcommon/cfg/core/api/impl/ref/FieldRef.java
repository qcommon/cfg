package net.dblsaiko.qcommon.cfg.core.api.impl.ref;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;

import net.dblsaiko.qcommon.cfg.core.api.ref.Ref;

public class FieldRef<T> implements Ref<T> {

    private final MethodHandle getter;
    private final MethodHandle setter;

    public FieldRef(Field field, Object target) {
        Lookup mh = MethodHandles.lookup();
        try {
            this.getter = mh.unreflectGetter(field).bindTo(target);
            this.setter = mh.unreflectSetter(field).bindTo(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get() {
        try {
            return (T) getter.invoke();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public void set(T value) {
        try {
            setter.invoke(value);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
