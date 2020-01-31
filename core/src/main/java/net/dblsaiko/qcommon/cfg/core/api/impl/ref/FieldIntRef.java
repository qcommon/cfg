package net.dblsaiko.qcommon.cfg.core.api.impl.ref;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;

import net.dblsaiko.qcommon.cfg.core.api.ref.IntRef;

public class FieldIntRef implements IntRef {

    private final MethodHandle getter;
    private final MethodHandle setter;

    public FieldIntRef(Field field, Object target) {
        if (field.getType() != int.class) {
            throw new IllegalArgumentException(String.format("Field %s is not of type int!", field));
        }
        Lookup mh = MethodHandles.lookup();
        try {
            MethodHandle getter = mh.unreflectGetter(field);
            MethodHandle setter = mh.unreflectSetter(field);
            if (target != null) {
                getter = getter.bindTo(target);
                setter = setter.bindTo(target);
            }
            this.getter = getter;
            this.setter = setter;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int get() {
        try {
            return (int) getter.invoke();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public void set(int value) {
        try {
            setter.invoke(value);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
