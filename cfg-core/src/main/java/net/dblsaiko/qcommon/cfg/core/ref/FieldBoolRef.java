package net.dblsaiko.qcommon.cfg.core.ref;

import net.dblsaiko.qcommon.cfg.core.api.ref.BoolRef;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;

public class FieldBoolRef implements BoolRef {

    private final MethodHandle getter;
    private final MethodHandle setter;

    public FieldBoolRef(Field field, Object target) {
        if (field.getType() != boolean.class) {
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
    public boolean get() {
        try {
            return (boolean) getter.invoke();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public void set(boolean value) {
        try {
            setter.invoke(value);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
